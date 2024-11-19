#!/bin/bash
#得到脚本所在目录
BASE_PATH=$(pwd)
#得到该目录下第一个jar包路径
TargetJar="${BASE_PATH}$(find "$BASE_PATH" -maxdepth 1 -type f -name "*.jar" | head -n 1)"
#日志输出目录
Log="${BASE_PATH}/run.log"
echo "日志输出路径:$Log"

EXIT_CODE="0"
# 启动jar包
function start() {
	# check running
	# shellcheck disable=SC2009
	pid=$(ps aux | grep "$TargetJar" | grep java | grep -v grep | awk '{print $2}')
	if [ "$pid" != "" ]; then
		echo "应用正在运行，请不要重复运行! -> $pid"
		exit 1
	fi
	echo "正在启动应用，日志目录 -> $Log"
	if [ ! -f "$Log" ]; then
		touch "$Log"
	fi
	# 启动命令
	nohup java -Xms512m -Xmx512m -jar "$TargetJar" > "$Log" 2>&1 &
	sleep 3
	head -n 100 "$Log"
}

function stop() {
	# shellcheck disable=SC2009
	pid=$(ps aux | grep "$TargetJar" | grep java | grep -v grep | awk '{print $2}')
	if [ "$pid" != "" ]; then
		echo -n "应用正在运行 -> $pid，即将停止"
		kill -15 "$pid"
		if [ "$pid" != "" ]; then
			kill -9 "$pid"
		fi
		echo "应用已停止 -> $pid"
	else
		echo "应用已停止"
	fi
}

function status() {
	# shellcheck disable=SC2009
	pid=$(ps aux | grep "$TargetJar" | grep java | grep -v grep | awk '{print $2}')
	if [ "$pid" != "" ]; then
		echo "应用正在运行:$pid"
	else
		echo "应用已停止"
	fi
}

# See how we were called.
EXIT_CODE="0"
case "$1" in
start)
	start
	;;
stop)
	stop
	;;
restart)
	stop
	start
	;;
status)
	status
	;;
*)
	stop
  start
  ;;
esac

exit $EXIT_CODE
