sudo -s chmod -R 777 /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG} || echo "Jenkins work directory is clean"
rm -R /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG} || echo "Jenkins work directory is clean"
mkdir -p /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG} || echo "Couldn't create work directory for project"
cp -R /home/wenqin1/gitrepo/* /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG}/
cd /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG}/tests

#cat > jenkins-test-env.sh <<EOF
#export MAG2_RAPI=${TESTS_HOST_IP}
#export MAG2HOST=${TESTS_HOST_IP}
#export MAG2_ADMINUSER=admin
#export MAG2_ADMINPASS=admin123
#export MAG2_TESTMODE=${TESTS_MAG2_TESTMODE}
#export SKIP_SETUP_BOX=yes
#export HTML_REPORT=${JENKINS_HTML_REPORT}
#export MAG2_HOST_TYPE=${TESTS_HOST_TYPE}
#export RELEASE_VERSION=${RELEASE_VERSION}
#export UPGRADE_PATH=${UPGRADE_PATH}
#
#export netmask=${netmask}
#export gateway=${gateway}
#export dns1=${dns1}
#export serial_switch=${serial_switch}
#export serial_port=${serial_port}
#export serial_ssh_base_port=${serial_ssh_base_port}
#export serial_user=${serial_user}
#export serial_password=${serial_password}
#export bmc_ip=${bmc_ip}
#export bmc_user=${bmc_user}
#export bmc_password=${bmc_password}
#EOF
#chmod +x jenkins-test-env.sh

docker kill project-${DEPLOY_CONFIG} || echo "Couldn't Kill the container"
docker rm project-${DEPLOY_CONFIG} || echo "Could not remove the container"
docker build --rm=true -t "bluecoat/project-${DEPLOY_CONFIG}" ./.jenkins
docker run --rm \
  --name "project-${DEPLOY_CONFIG}" \
  -v "$(pwd)/output":"/build/output" \
  -v "$(pwd)":"/git/tests" \
  -v "/var/lib/jenkins:/var/lib/jenkins":ro \
  -e "JENKINS_USER=$(id -un)" \
  -e "JENKINS_UID=$(id -u)" \
  -e "JENKINS_GROUP=$(id -gn)" \
  -e "JENKINS_GID=$(id -g)"\
  -e "RELEASE_VERSION=${RELEASE_VERSION}" \
  -e "UPGRADE_PATH=${UPGRADE_PATH}" \
  -e "DEPLOY_CONFIG=${DEPLOY_CONFIG}" \
  "bluecoat/project-${DEPLOY_CONFIG}" \
  /git/tests/.jenkins/run.sh
