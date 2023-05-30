1. Download and install [Tanzu CLI](https://docs.vmware.com/en/VMware-Tanzu-Application-Platform/1.5/tap/install-tanzu-cli.html).

2. Download the TAP CLI plugins and install them following instructions in the link above.

	```
	$ tar -xvf ~/Downloads/tanzu-framework-linux-amd64-v0.28.1.3.tar -C ~/tanzu
	$ cd ~/tanzu
	$ tanzu plugin install --local cli all
	```

3. Check it works

	```
	$ tanzu version
	$ tanzu apps workload list
	No workloads found.
	```

4. Deploy a workload.

	```
	$ tanzu apps workload apply -f config/workload.yaml
	```

5. Install VSCode tools from the VSIX downloaded per the instructions in [Tanzu VSCode docs](https://docs.vmware.com/en/VMware-Tanzu-Application-Platform/1.5/tap/vscode-extension-about.html).