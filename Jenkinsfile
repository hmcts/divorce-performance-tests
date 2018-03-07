#!groovy

//noinspection GroovyAssignabilityCheck Jenkins requires this format
properties(
  [[$class: 'GithubProjectProperty', displayName: 'Divorce Performance tests', projectUrlStr: 'https://github.com/hmcts/div-performance-tests/'],
   pipelineTriggers([
     [$class: 'hudson.triggers.TimerTrigger', spec: '0 0 * * *']
   ])]
)

@Library(['Reform', 'Divorce']) _

_env_vars = []

buildNode() {
    timeout(time: 30, unit: 'MINUTES') {

        ws('workspace/divorce-performance-test') {
            println environmentBillboard()

            stage('Result') {
                deleteDir()
                git url: 'https://github.com/hmcts/divorce-performance-tests.git'
                cloneAnsibleDivorce()
                env_vars( environment() )

                def home = pwd()
                try {
                    env.BASE_URL = 'https://www-dev.divorce.reform.hmcts.net/index'
                    env.IDAM_URL = 'https://idam-test.dev.ccidam.reform.hmcts.net'
                    sh "mvn clean install"
                    sh "mvn gatling:execute"
                }
                finally {
                    gatlingArchive()
                    sh "mv ${home}/results/divorce*/* ${home}/results"
                    publishHTML target: [
                            alwaysLinkToLastBuild: true,
                            reportDir            : "${home}/results",
                            reportFiles          : "*",
                            reportName           : "Performance test report",
                            keepAll              : true
                    ]
                }
            }
        }
    }
}

def environment() {
    if (ENVIRONMENT in ['dev', 'test', 'demo', 'preprod']) {
        return ENVIRONMENT
    } else {
        error "$ENVIRONMENT is not one of reforms crossbrowser environments"
    }
}

def environmentBillboard() {
    def environmentDetail = "## ${environment().toUpperCase()} ENVIRONMENT ##"
    def border = ""
    (1..environmentDetail.size()).each {
        border += "#"
    }
    return "$border\n$environmentDetail\n$border"
}

def frontend_url() {
    def url = env_vars().get('divorce_frontend_hostname')
    def protocol = env_vars().get('divorce_frontend_protocol')

    def result = "$protocol://$url"
    println "frontend_url = $result"
    return result
}

def idam_url() {
    def login_url = env_vars().get('idam').get('login_url')
    def stripped_login_url = login_url.substring(0, login_url.lastIndexOf('/login'))

    println "idam_url = $stripped_login_url"
    return stripped_login_url
}

def ansibleLocation() {
    return 'ansible-divorce'
}

def script_workspace() {
    return "${pwd()}/${ansibleLocation()}"
}

def env_vars(String env = 'dev') {
    if (_env_vars == []) {
        _env_vars = readYaml file: "${script_workspace()}/env_vars/${env}.yml"
    }
    return _env_vars
}

def cloneAnsibleDivorce() {
    sh "git clone git@git.reform.hmcts.net:divorce/ansible-divorce.git ./${ansibleLocation()}"
}
