String basePath = 'upgradeAutomation'

folder(basePath) {
    description 'This example shows how to include script resources from the workspace for CASMA upgrade automation.'
}

[
    [uPath: 'direct', DEPLOY_CONFIG: 'kl.cas-s500-a1-01', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1::latest:'], 
    [uPath: 'incremental', DEPLOY_CONFIG: 'kl.cas-s400-a4-03', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1::2.2.1.1:2.3.1.1::2.3.1.2::latest:'],
    [uPath: '1.3.7.x-2.1.1-2.3.5', DEPLOY_CONFIG: 'kl.cas-s400-a1-10', 
     UPGRADE_PATH: '1.3.7.1::2.1.1.1::latest:']
].each { Map config ->

    job("$basePath/upgrade-${config.uPath}") {

        description "${config.uPath} upgrade job"

        parameters {
            stringParam('DEPLOY_CONFIG', "${config.DEPLOY_CONFIG}")
            stringParam('RELEASE_VERSION', "2.3.5.1")
            stringParam('UPGRADE_PATH', "${config.UPGRADE_PATH}")
        }

        triggers {
            cron('H/2 * * * *')
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
