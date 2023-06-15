# Deploying PetClinic to TAP

## Up and Running

1. Deploy a database and set up a secret for the service binding:

	```
	$ kubectl apply -f config/database.yaml
	```

2. Deploy a workload.

	```
	$ kubectl apply -f config/workload.yaml
	```

	(same as)

	```
	$ tanzu apps workload apply -f config/workload.yaml
	```

3. Install VSCode tools from the VSIX downloaded per the instructions in [Tanzu VSCode docs](https://docs.vmware.com/en/VMware-Tanzu-Application-Platform/1.5/tap/vscode-extension-about.html).

## TAP Basics

### Tanzu CLI

You don't need the Tanzu CLI to use TAP, but it's a convenient way to get started, and it's the only supported way to manage the platform features.

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

### Config Values

If you see references to `tap-values.yml` in the docs, it's here:

```
$ kubectl get secret tap-values -n tap-install --template='{{index .data "tap-values.yaml"}}' | base64 -d > tap-values.yaml
```

You can edit that file and apply it to the cluster:

```
$ tanzu package installed update tap -n tap-install --values-file tap-values.yaml
```