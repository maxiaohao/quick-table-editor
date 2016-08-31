SHELL = /bin/sh

init:
	rm -rf src/main/webapp/third_party
	bower install

init-eclipse:
	gradle clean cleanEclipse eclipse

war: 
	gradle war

check:
	gradle check #cobertura coberturaCheck

run:
	gradle jettyRun

stop:
	gradle jettyStop
	
clean:
	gradle clean
	rm -rf node_modules src/main/webapp/third_party build bin

