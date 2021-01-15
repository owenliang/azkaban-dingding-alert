#!/bin/bash

# 编译jar
mvn clean package

# 把jar放入plugin/lib目录
cp -f ./target/azkaban-dingding-alert-1.0.jar ./doc/azkaban-dingding-alert/lib/
# 把plugin目录整体打包
cd ./doc && tar czvf azkaban-dingding-alert.tar.gz azkaban-dingding-alert && mv ./azkaban-dingding-alert.tar.gz .. && cd -