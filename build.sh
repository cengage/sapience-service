#!/bin/bash
# ********************************************************************************
# *
# *         Script Author: Christopher Martello
# *           Create Date: May 2nd, 2014
# *    Last Modified Date:  
# *
# * Project / Application: Master Build Script for Sapience Service
# *   Objective / Purpose: joint build script
# *
# *  Script / Action Name: Build
# *
# **********************************************************************************
localMongo=false;
#-------------------------------------------------------
#-- Functions Section
#-------------------------------------------------------

function mongo() {
    echo "";
    echo "#-----   L A U N C H I N G   M O N G O  -----";
    # This mongod run line is special. Do not modify. configure your mongo DB data location as below.
    exec mongod --dbpath /servers/data/mongo/ &
    localMongo=true;
}

function build() {
    echo "";
	echo "#-----   B U I L D I N G    -----";
	rm -rf node_modules .bower-cache .bower-registry app/lib/*;
	npm --version;
	npm install -f;
	grunt --version;
	grunt build;
}

function publish() {
    echo "";
    echo "-----   P U B L I S H I N G    -----";
    grunt publish;
}

function server() {
    echo "";
    echo "#-----   L A U N C H I N G   S E R V E R -----";
    grunt server &
}

function mongo_check() {
    if [ $localMongo == false ]; then
        echo "#----- MONGO DB not running Locally. Let it ride.... ";

    else
        echo "";
        echo "#-----  S T O P P I N G     M O N G O  -----";
        exec mongo --nodb --eval "connect('localhost:27017/admin').shutdownServer()"& 
        echo "";
        echo "#-----  M O N G O  S T O P P E D    -----";
        echo "";
    fi
}

function exit_code_check() {
    rc=$?
    if [[ $rc != 0 ]] ; then
        echo "";
        echo "#-----     Exit Code ERROR FOUND      -----";
        exit $rc; 
    fi
}

# -- Check for arguments --
if [ $# -gt 0 ]; then
    #continue on your merry way 
    echo "";
else
	echo "";
    echo "Your command line contains no arguments";
    echo "Please indicate -b (build), -s (server) , -d (deploy), -m (mongo) commands.";
    echo "";
    echo "For Local Builds, run with -m -b ";
    echo "";
    echo "For a Run with Server, use -m -b -s";
    echo "";
    echo "For a Jenkins CI build, use -p";
    echo "";
    exit 0;
fi
#-------------------------------------------------------
#-- Handle arguments and do the script work here
#-------------------------------------------------------
while getopts "mbsp" OPTION ;do
    case $OPTION in
        m) 
            mongo
            ;;

        b)
            build
            ;;
        s)
			server
            ;;
        p) 
            publish
            ;;
        ?)
			echo "";
			echo "Incorrect parameter passed. Parameters are:" ;
            echo "-m for mongo start for a local build"
			echo "-b for Build";
            echo "-p for Publish Artifact Graph to nexus";
			echo "-s for Launch Server";
            echo "";
            exit 0 ;
            ;;
    esac
    #--- Check the exit code, and spit out the completed banner
    exit_code_check;
    echo "#-----     C O M P L E T E D    -----"
    echo "";
done
   mongo_check;
exit 0;