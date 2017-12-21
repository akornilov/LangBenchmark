using build

class Build : BuildPod {

	new make() {
		podName = "fantom"
		summary = "My Awesome FantomTest Project"
		version = Version("1.0")

		meta = [
			"proj.name" : "Fantom"
		]

		depends = [
			"sys 1.0"
		]

		srcDirs = [`fan/`]
		resDirs = [,]

		docApi = true
		docSrc = false
	}
}
