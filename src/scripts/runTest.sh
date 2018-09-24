sudo -s chmod -R 777 /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG} || echo "Jenkins work directory is clean"
rm -R /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG} || echo "Jenkins work directory is clean"
mkdir -p /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG} || echo "Couldn't create work directory for project"
cp -R /home/wenqin1/gitrepo/* /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG}/
cd /home/wenqin1/Jenkins-Work-Directory/project-${DEPLOY_CONFIG}/tests

docker kill project-${DEPLOY_CONFIG} || echo "Couldn't Kill the container"
docker rm project-${DEPLOY_CONFIG} || echo "Could not remove the container"
docker build --rm=true -t "bluecoat/project-${DEPLOY_CONFIG}" ./.jenkins
docker run --rm \
  --name "project-${DEPLOY_CONFIG}" \
  -p ${PORT_FWD}:5908 \
  -v "$(pwd)/output":"/build/output" \
  -v "$(pwd)":"/git/tests" \
  -v "/var/lib/jenkins:/var/lib/jenkins":ro \
  -e "RELEASE_VERSION=${RELEASE_VERSION}" \
  -e "UPGRADE_PATH=${UPGRADE_PATH}" \
  -e "DEPLOY_CONFIG=${DEPLOY_CONFIG}" \
  "bluecoat/project-${DEPLOY_CONFIG}" \
  /git/tests/.jenkins/run-upgrade-suite.sh
