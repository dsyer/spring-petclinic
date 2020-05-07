# Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-projects/spring-petclinic.png?branch=main)](https://travis-ci.org/spring-projects/spring-petclinic/)

## Understanding the Spring Petclinic application with a few diagrams
<a href="https://speakerdeck.com/michaelisvy/spring-petclinic-sample-application">See the presentation here</a>

## Running petclinic locally
Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line (it should work just as well with Java 8, 11 or 17):


```
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
./mvnw package
java -jar target/*.jar
```

You can then access petclinic here: http://localhost:8080/

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

> NOTE: Windows users should set `git config core.autocrlf true` to avoid format assertions failing the build (use `--global` to set that flag globally).

## Benchmarks

Java 8 and 14 (with `-noverify`) Boot 2.2.6:

```
jdk  class               method  profile  sample  beans    classes   heap    memory  median  mean   range
8    PetClinicBenchmark  main    demo     auto    212.000  6719.000  11.383  78.911  1.207   1.235  0.028
8    PetClinicBenchmark  main    demo     manual  130.000  5998.000  9.819   73.411  0.983   1.000  0.017
8    PetClinicBenchmark  main    actr     auto    359.000  7744.000  15.034  88.252  1.508   1.561  0.053
8    PetClinicBenchmark  main    actr     manual  180.000  7092.000  13.326  86.459  1.079   1.099  0.035
14   PetClinicBenchmark  main    demo     auto    212.000  7251.000  15.497  84.219  1.144   1.160  0.014
14   PetClinicBenchmark  main    demo     manual  130.000  6529.000  13.855  77.496  0.928   0.941  0.018
14   PetClinicBenchmark  main    actr     auto    359.000  8221.000  18.458  93.165  1.442   1.481  0.036
14   PetClinicBenchmark  main    actr     manual  180.000  7036.000  15.061  82.011  1.027   1.043  0.036
```

CDS, Java 14:

```
class         method  profile  sample  beans    classes    heap    memory  median  mean   range
CdsBenchmark  main    demo     auto    213.000  9110.000   19.965  67.231  0.927   0.953  0.032
CdsBenchmark  main    demo     manual  130.000  8486.000   17.600  63.808  0.734   0.748  0.017
CdsBenchmark  main    actr     auto    361.000  9904.000   23.330  71.879  1.149   1.166  0.027
CdsBenchmark  main    actr     manual  180.000  9661.000   19.260  68.564  0.807   0.822  0.017
```

(the ramdisk didn't really add much this time, even though it seemed to work better with Java 11).


Java 8 (with `-noverify`), Boot 2.2.1:

```
class            method profile sample    beans  classes     heap  memory  median  mean  range
PetClinicBenchmark  main  demo  auto    216.000  7886.000  14.715  93.292  1.319  1.348  0.042
PetClinicBenchmark  main  demo  manual  133.000  7273.000  13.410  88.684  1.109  1.126  0.024
PetClinicBenchmark  main  actr  auto    364.000  8921.000  17.645  102.354 1.702  1.719  0.015
PetClinicBenchmark  main  actr  manual  182.000  7580.000  13.457  90.966  1.201  1.216  0.019
```

Boot 2.2 after M4:

```
class           method  profile sample  beans    classes     heap  memory  median  mean  range
PetClinicBenchmark  main  demo  auto    217.000  7891.000  15.000  93.475  1.323  1.348  0.058
PetClinicBenchmark  main  demo  manual  135.000  7300.000  13.624  89.072  1.102  1.120  0.019
PetClinicBenchmark  main  actr  auto    360.000  8938.000  17.888  102.299 1.695  1.716  0.029
PetClinicBenchmark  main  actr  manual  181.000  7572.000  13.576  90.968  1.187  1.197  0.029
```

Older results:

```
class             method  profile sample  beans    classes    heap  memory  median  mean  range
PetClinicBenchmark  main  demo   auto    374.000  9036.000  18.523  105.637  1.844  1.862  0.020
PetClinicBenchmark  main  demo   manual  198.000  7851.000  14.596  95.148   1.322  1.341  0.027
PetClinicBenchmark  main  actr   auto    374.000  9047.000  18.721  105.976  1.837  1.862  0.030
PetClinicBenchmark  main  actr   manual  198.000  7849.000  14.679  95.260   1.319  1.340  0.019
PetClinicBenchmark  main  first  auto    374.000  11683.000 24.626  128.594  2.857  2.877  0.023
PetClinicBenchmark  main  first  manual  198.000  9940.000  19.028  112.113  2.025  2.056  0.035
PetClinicBenchmark  main  init   auto    232.000  10119.000 22.811  117.887  2.119  2.164  0.082
PetClinicBenchmark  main  init   manual  132.000  9259.000  19.701  107.627  1.860  1.907  0.054
```

Java 8 (without `-noverify`):

```
class             method  profile  sample  beans  classes     heap  memory  median  mean  range
PetClinicBenchmark  main  demo   auto    374.000  9348.000  18.023  114.565  2.570  2.660  0.080
PetClinicBenchmark  main  demo   manual  198.000  8339.000  14.566  106.076  1.921  2.025  0.100
PetClinicBenchmark  main  actr   auto    374.000  9316.000  17.623  113.907  2.628  2.692  0.057
PetClinicBenchmark  main  actr   manual  198.000  8396.000  14.429  105.720  1.973  2.028  0.046
PetClinicBenchmark  main  first  auto    374.000  12752.000 25.625  144.909  4.227  4.389  0.186
PetClinicBenchmark  main  first  manual  198.000  10971.000 19.571  125.674  2.992  3.092  0.132
```

Java 11 (without `-noverify`):

```
class             method  profile sample  beans     classes  heap    memory  median  mean  range
PetClinicBenchmark  main  demo    auto    374.000  9825.000  21.298  119.357  2.648  2.719  0.084
PetClinicBenchmark  main  demo    manual  198.000  8854.000  18.224  109.570  1.963  2.026  0.051
PetClinicBenchmark  main  actr    auto    374.000  9856.000  21.297  119.399  2.693  2.744  0.048
PetClinicBenchmark  main  actr    manual  198.000  8748.000  18.176  109.289  1.947  2.024  0.077
PetClinicBenchmark  main  first   auto    374.000  13309.000 28.881  152.264  4.167  4.254  0.121
PetClinicBenchmark  main  first   manual  198.000  11513.000 23.209  133.014  2.876  3.012  0.111
CdsBenchmark        main  demo    auto    374.000  11102.000 21.504  80.633   1.959  2.016  0.058
CdsBenchmark        main  demo    manual  198.000  9284.000  18.169  75.055   1.319  1.429  0.104
CdsBenchmark        main  actr    auto    374.000  11104.000 21.504  80.675   1.934  2.019  0.102
CdsBenchmark        main  actr    manual  198.000  9284.000  18.210  76.389   1.350  1.411  0.057
```

Java 11 with CDS cache in a ramdisk(!):

```
class       method  profile sample  beans     classes    heap  memory  median  mean  range
CdsBenchmark  main  demo    auto    216.000  9929.000  18.568  67.513  1.053  1.080  0.026
CdsBenchmark  main  demo    manual  136.000  8678.000  16.848  68.769  0.833  0.859  0.026
CdsBenchmark  main  actr    auto    359.000  10961.000 20.943  71.135  1.288  1.347  0.053
CdsBenchmark  main  actr    manual  182.000  9385.000  17.942  69.740  0.928  0.943  0.018
```

How to create a ramdisk: 

```
$ sudo mkdir /media/ramdisk
$ sudo mount -t ramfs -o size=512m ramfs /media/ramdisk
$ sudo chmod 777 /media/ramdisk
```

## In case you find a bug/suggested improvement for Spring Petclinic
Our issue tracker is available here: https://github.com/spring-projects/spring-petclinic/issues


## Database configuration

In its default configuration, Petclinic uses an in-memory database (H2) which
gets populated at startup with data. The h2 console is automatically exposed at `http://localhost:8080/h2-console`
and it is possible to inspect the content of the database using the `jdbc:h2:mem:testdb` url.
 
A similar setup is provided for MySql in case a persistent database configuration is needed. Note that whenever the database type is changed, the app needs to be run with a different profile: `spring.profiles.active=mysql` for MySql.

You could start MySql locally with whatever installer works for your OS, or with docker:

```
docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7.8
```

Further documentation is provided [here](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/mysql/petclinic_db_setup_mysql.txt).

## Working with Petclinic in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 8 or newer (full JDK not a JRE).
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)

### Steps:

1) On the command line
    ```
    git clone https://github.com/spring-projects/spring-petclinic.git
    ```
2) Inside Eclipse or STS
    ```
    File -> Import -> Maven -> Existing Maven project
    ```

    Then either build on the command line `./mvnw generate-resources` or using the Eclipse launcher (right click on project and `Run As -> Maven install`) to generate the css. Run the application main method by right clicking on it and choosing `Run As -> Java Application`.

3) Inside IntelliJ IDEA
    In the main menu, choose `File -> Open` and select the Petclinic [pom.xml](pom.xml). Click on the `Open` button.

    CSS files are generated from the Maven build. You can either build them on the command line `./mvnw generate-resources` or right click on the `spring-petclinic` project then `Maven -> Generates sources and Update Folders`.

    A run configuration named `PetClinicApplication` should have been created for you if you're using a recent Ultimate version. Otherwise, run the application by right clicking on the `PetClinicApplication` main class and choosing `Run 'PetClinicApplication'`.

4) Navigate to Petclinic

    Visit [http://localhost:8080](http://localhost:8080) in your browser.


## Looking for something in particular?

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [PetClinicApplication](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java) |
|Properties Files | [application.properties](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources) |
|Caching | [CacheConfiguration](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/CacheConfiguration.java) |

## Interesting Spring Petclinic branches and forks

The Spring Petclinic "main" branch in the [spring-projects](https://github.com/spring-projects/spring-petclinic)
GitHub org is the "canonical" implementation, currently based on Spring Boot and Thymeleaf. There are
[quite a few forks](https://spring-petclinic.github.io/docs/forks.html) in a special GitHub org
[spring-petclinic](https://github.com/spring-petclinic). If you have a special interest in a different technology stack
that could be used to implement the Pet Clinic then please join the community there.


## Interaction with other open source projects

One of the best parts about working on the Spring Petclinic application is that we have the opportunity to work in direct contact with many Open Source projects. We found some bugs/suggested improvements on various topics such as Spring, Spring Data, Bean Validation and even Eclipse! In many cases, they've been fixed/implemented in just a few days.
Here is a list of them:

| Name | Issue |
|------|-------|
| Spring JDBC: simplify usage of NamedParameterJdbcTemplate | [SPR-10256](https://jira.springsource.org/browse/SPR-10256) and [SPR-10257](https://jira.springsource.org/browse/SPR-10257) |
| Bean Validation / Hibernate Validator: simplify Maven dependencies and backward compatibility |[HV-790](https://hibernate.atlassian.net/browse/HV-790) and [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: provide more flexibility when working with JPQL queries | [DATAJPA-292](https://jira.springsource.org/browse/DATAJPA-292) |


# Contributing

The [issue tracker](https://github.com/spring-projects/spring-petclinic/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <https://editorconfig.org>. If you have not previously done so, please fill out and submit the [Contributor License Agreement](https://cla.pivotal.io/sign/spring).

# License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).

[spring-petclinic]: https://github.com/spring-projects/spring-petclinic
[spring-framework-petclinic]: https://github.com/spring-petclinic/spring-framework-petclinic
[spring-petclinic-angularjs]: https://github.com/spring-petclinic/spring-petclinic-angularjs 
[javaconfig branch]: https://github.com/spring-petclinic/spring-framework-petclinic/tree/javaconfig
[spring-petclinic-angular]: https://github.com/spring-petclinic/spring-petclinic-angular
[spring-petclinic-microservices]: https://github.com/spring-petclinic/spring-petclinic-microservices
[spring-petclinic-reactjs]: https://github.com/spring-petclinic/spring-petclinic-reactjs
[spring-petclinic-graphql]: https://github.com/spring-petclinic/spring-petclinic-graphql
[spring-petclinic-kotlin]: https://github.com/spring-petclinic/spring-petclinic-kotlin
[spring-petclinic-rest]: https://github.com/spring-petclinic/spring-petclinic-rest
