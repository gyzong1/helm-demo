node {
    def server = Artifactory.server 'art1'
    def rtMaven = Artifactory.newMavenBuild()
    def buildInfo

    stage ('Clone') {
        git url: 'https://github.com/gyzong1/helm-demo.git'
    }
    
    stage ('Artifactory configuration') {
        rtMaven.tool = 'maven3' // Tool name from Jenkins configuration
        rtMaven.deployer releaseRepo: 'maven-dev-local', snapshotRepo: 'maven-dev-local', server: server
        rtMaven.resolver releaseRepo: 'maven-virtual', snapshotRepo: 'maven-virtual', server: server
        buildInfo = Artifactory.newBuildInfo()
    }

    stage ('Exec Maven') {
        rtMaven.run pom: 'pom.xml', goals: 'clean install', buildInfo: buildInfo
    }

    stage ('Publish build info') {
        server.publishBuildInfo buildInfo
    }
}
