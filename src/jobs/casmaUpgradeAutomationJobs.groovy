String basePath = 'upgradeAutomation'

folder(basePath) {
    description 'This example shows how to include script resources from the workspace for CASMA upgrade automation.'
}

job("$basePath/run-automation-job") {

    parameters {
        stringParam('RELEASE_VERSION', '2.3.5.1', 'Build version under test')
        stringParam('UPGRADE_PATH', '1.3.7.1::2.1.1.1')
        stringParam('DEPLOY_CONFIG', 'kl.cas-s400-a4-03')
        stringParam('TESTS_HOST_IP', '10.199.110.73')
        stringParam('TESTS_MAG2_TESTMODE', 'casma')
        stringParam('TESTS_HOST_TYPE', 's400')
        stringParam('JENKINS_HTML_REPORT', 'disabled')
        stringParam('netmask', '255.255.255.0')
        stringParam('gateway', '10.199.110.253')
        stringParam('dns', '8.8.8.8')
        stringParam('serial_switch', '10.199.97.23')
        stringParam('serial_port', '9')
        stringParam('serial_ssh_base_port', '5100')
        stringParam('serial_user', 'administrator')
        stringparam('serial_password', 'password')
        stringParam('bmc_ip', '10.199.112.73')
        stringParam('bmc_user', 'admin')
        stringParam('bmc_password', 'password')

    }
    steps {
        shell readFileFromWorkspace('src/scripts/runTest.sh')
    }

    publishers {
        junit {
            pattern('project-${DEPLOY_CONFIG}/tests/Reports/*.xml')
        }
    }
}

