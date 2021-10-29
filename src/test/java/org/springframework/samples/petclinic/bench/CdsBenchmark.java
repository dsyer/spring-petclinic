/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.bench;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;

import jmh.mbr.junit5.Microbenchmark;
import org.openjdk.jmh.annotations.AuxCounters;
import org.openjdk.jmh.annotations.AuxCounters.Type;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.util.FileUtils;

import org.springframework.boot.loader.jar.JarFile;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.test.ManualConfigApplication;
import org.springframework.util.StreamUtils;

@Measurement(iterations = 5, time = 1)
@Warmup(iterations = 1, time = 1)
@Fork(value = 2, warmups = 0)
@BenchmarkMode(Mode.AverageTime)
@Microbenchmark
public class CdsBenchmark {

	@Benchmark
	public void main(CdsState state) throws Exception {
		state.run();
	}

	@State(Scope.Thread)
	@AuxCounters(Type.EVENTS)
	public static class CdsState extends ProcessLauncherState {

		public static enum Profile {

			demo, actr;

		}

		public static enum Sample {

			auto, manual(ManualConfigApplication.class);

			private Class<?> config;

			private Sample(Class<?> config) {
				this.config = config;
			}

			private Sample() {
				this.config = PetClinicApplication.class;
			}

			public Class<?> getConfig() {
				return config;
			}

		}

		private static final String APP_JSA = "app.jsa";

		@Param // ("auto")
		Sample sample = Sample.auto;

		@Param // ({ "demo" })
		Profile profile = Profile.demo;

		@Override
		public int getClasses() {
			return super.getClasses();
		}

		@Override
		public int getBeans() {
			return super.getBeans();
		}

		@Override
		public double getMemory() {
			return super.getMemory();
		}

		@Override
		public double getHeap() {
			return super.getHeap();
		}

		public CdsState() {
			super("target", "--server.port=0");
		}

		@Override
		protected void customize(List<String> args) {
			args.addAll(Arrays.asList("-Xshare:on", // "-XX:+UseAppCDS",
					"-XX:SharedArchiveFile=" + APP_JSA));
			super.customize(args);
		}

		@TearDown(Level.Invocation)
		public void stop() throws Exception {
			super.after();
		}

		@Setup(Level.Trial)
		public void start() throws Exception {
			if (profile != Profile.demo) {
				setProfiles(profile.toString());
			}
			String cp = getClasspath();
			StringBuilder builder = new StringBuilder();
			for (String jar : cp.split(":")) {
				try (JarFile jarfile = new JarFile(new File(jar))) {
					for (JarEntry entry : jarfile) {
						String name = entry.getName();
						if (name.endsWith(".class") && !name.equals("module-info.class")
								&& !name.contains("ThinJarWrapper")) {
							name = name.replace("/", ".").replace(".class", "");
							builder.append(name).append("\n");
						}
					}
				}
			}
			StreamUtils.copy(builder.toString().getBytes(), new FileOutputStream("target/app.classlist"));
			setMainClass(sample.getConfig().getName());
			Process dump = exec(new String[] { "-Xshare:dump", // "-XX:+UseAppCDS",
					"-XX:SharedClassListFile=app.classlist", "-XX:SharedArchiveFile=" + APP_JSA, "-cp", "" });
			System.err.println(FileUtils.readAllLines(dump.getInputStream()));
			dump.waitFor();
			System.err.println("Finished dumping class data");
			super.before();
		}

		@Override
		protected String getClasspath() {
			return getClasspath(false);
		}

	}

}
