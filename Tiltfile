SOURCE_IMAGE = os.getenv("SOURCE_IMAGE", default='index.docker.io/dsyer/petclinic-src')
LOCAL_PATH = os.getenv("LOCAL_PATH", default='.')
NAMESPACE = os.getenv("NAMESPACE", default='my-apps')

k8s_custom_deploy(
	'petclinic',
	apply_cmd="tanzu apps workload apply -f config/workload.yaml --update-strategy replace --debug --live-update" +
		" --local-path " + LOCAL_PATH +
		" --source-image " + SOURCE_IMAGE +
		" --namespace " + NAMESPACE +
		" --yes --output yaml", 
	delete_cmd="tanzu apps workload delete -f config/workload.yaml --namespace " + NAMESPACE + " --yes", 
	deps=['pom.xml', './target/classes'],
	container_selector='workload',
	live_update=[
		sync('./target/classes', '/workspace/BOOT-INF/classes')
	]
)

k8s_resource('petclinic', port_forwards=["8080:8080"],
	extra_pod_selectors=[{'carto.run/workload-name': 'petclinic', 'app.kubernetes.io/component':'run'}])

allow_k8s_contexts('dsyer-tap-demo')