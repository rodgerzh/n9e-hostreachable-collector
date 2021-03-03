#!/bin/sh
#created by zhangjian
cd `dirname $0`
cd ..

if [ -z "$JAVA_HOME" -a -z "$JRE_HOME" ]; then
  #标准的java路径
  if [ -x /usr/bin/java ]; then
    JRE_HOME=/usr
  fi
  #常用java路径
  if [ -z "$JRE_HOME" ]; then
    JAVA_PATH=`dirname /opt/jre*/bin/java 2>/dev/null|head -n 1`
	JRE_HOME=`dirname $JAVA_PATH 2>/dev/null|head -n 1`
	if [ -x "$JRE_HOME/bin/java" ]; then
      echo "Guess JRE_HOME is:$JRE_HOME"
	else
	  JRE_HOME=""
    fi
  fi
  if [ -z "$JRE_HOME" ]; then
    JAVA_PATH=`dirname /opt/jdk*/jre/bin/java 2>/dev/null|head -n 1`
	JRE_HOME=`dirname "$JAVA_PATH" 2>/dev/null|head -n 1`
	if [ -x "$JRE_HOME/bin/java" ]; then
      echo "Guess JRE_HOME is:$JRE_HOME"
	else 
	  JRE_HOME=""
    fi
  fi
  if [ -z "$JRE_HOME" ]; then
    JAVA_PATH=`dirname /opt/jdk*/bin/java 2>/dev/null|head -n 1`
	JRE_HOME=`dirname "$JAVA_PATH" 2>/dev/null|head -n 1`
	if [ -x "$JRE_HOME/bin/java" ]; then
	  echo "Guess JRE_HOME is:$JRE_HOME"
	else
      JRE_HOME=""
    fi
  fi
fi
if [ -z "$JRE_HOME" ]; then
  JRE_HOME="$JAVA_HOME"
  echo "Guess JRE_HOME is:$JRE_HOME"
fi
if [ -z "$JRE_HOME" ]; then
  echo "Neither the JAVA_HOME nor the JRE_HOME environment variable is defined"
  exit 1
fi

CLASSPATH="./bin"
for eachjar in `ls ./lib/*.jar -1`; do
  CLASSPATH="$CLASSPATH:$eachjar"
done

MAINCLASS="n9e.hr.main.HostReachableCollectorMain"
echo "MAINCLASS:$MAINCLASS"

nohup "$JRE_HOME"/bin/java -Duser.timezone=GMT+08 -DlogLevel=info -classpath "$CLASSPATH" $MAINCLASS >/dev/null 2>&1 &
pid=$!
echo "pid:${pid}"

sleep 2
ps -eo pid|grep -w $pid
ps_ret_code=$?

if [ $ps_ret_code -ne 0 ]; then
  echo "After start, the process exited."
  echo "Start failed!"
  exit 1
fi

