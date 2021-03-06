String basePath = 'upgradeAutomation'

folder(basePath) {
    description 'This example shows how to include script resources from the workspace for CASMA upgrade automation.'
}

[
    [uPath: 'direct', 
     DEPLOY_CONFIG: 'kl.cas-s500-a1-01', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1_debug::2.3.5.1_debug::latest:casma_2_4_debug_bcsi', 
     SCHEDULE_TIME: 'H H(5-7) * * *', 
     PORT_FWD: '5908', 
     SAMPLE_REPO: 'http://10.199.97.67/sample/automation'], 
    [uPath: 'incremental', 
     DEPLOY_CONFIG: 'kl.cas-s400-a4-03', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1_debug::2.2.1.1_debug::2.3.1.1_debug::2.3.1.2_debug::2.3.5.1_debug::latest:casma_2_4_debug_bcsi', 
     SCHEDULE_TIME: 'H H(5-7) * * *', 
     PORT_FWD: '5907', 
     SAMPLE_REPO: 'http://10.199.97.67/sample/automation'],
    [uPath: '1.3.7.x-2.1.1-2.2.1-2.4.1', 
     DEPLOY_CONFIG: 'kl.cas-s500-a1-01', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1_debug::2.2.1.1_debug::latest:casma_2_4_debug_bcsi', 
     SCHEDULE_TIME: 'H H(1-3) * * *', 
     PORT_FWD: '5906', 
     SAMPLE_REPO: 'http://10.199.97.67/sample/automation'], 
    [uPath: '1.3.7.x-2.1.1-2.3.1-2.4.1', 
     DEPLOY_CONFIG: 'kl.cas-s400-a4-03', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1_debug::2.3.1.1_debug::latest:casma_2_4_debug_bcsi', 
     SCHEDULE_TIME: 'H H(1-3) * * *', 
     PORT_FWD: '5905', 
     SAMPLE_REPO: 'http://10.199.97.67/sample/automation']
].each { Map config ->

    job("$basePath/upgrade-${config.uPath}") {

        description "${config.uPath} upgrade job"

        parameters {
            stringParam('DEPLOY_CONFIG', "${config.DEPLOY_CONFIG}")
            stringParam('RELEASE_VERSION', "2.4.1.1")
            stringParam('UPGRADE_PATH', "${config.UPGRADE_PATH}")
            stringParam('SCHEDULE_TIME', "${config.SCHEDULE_TIME}")
            stringParam('PORT_FWD', "${config.PORT_FWD}")
            stringParam('SAMPLE_REPO', "${config.SAMPLE_REPO}")
        }

        triggers {
            cron("${config.SCHEDULE_TIME}")
        }

        customWorkspace('/home/wenqin1/Jenkins-Work-Directory/')

        steps {
            shell readFileFromWorkspace('src/scripts/runTest.sh')
        }

        publishers {
            archiveJunit("project-${config.DEPLOY_CONFIG}/tests/Reports/*.xml") {
               retainLongStdout()
               healthScaleFactor(1.0)
            }
        }
    }
}
