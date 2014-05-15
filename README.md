![icon](QAMetrix-Logo.png)

## Sapience Service
A NodeJS Server API Interface for the QA Metrix project.

## Install Pre-Requisite Apps on Local
install	brew

	ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/homebrew/go/install)"

install mongo db - <http://docs.mongodb.org/manual/tutorial/install-mongodb-on-os-x/>
	
	brew update
	brew install mongodb

You'll need to configure the data directory for your mongoDB 

install node

	brew install node
	
install git

	sudo npm install -g git

install	bower

	sudo npm install -g bower

install grunt

	sudo npm install -g grunt
	sudo npm install -g grunt-cli

### Start Here if you already have DEV packages installed

Now clone the App Repo

	mkdir /sapience
	cd /sapeience
	git clone https://github.com/cengage/sapience-service.git

At the command line of the app directory
	
	cd {to-your-dir-here}  /sapience/sapience-service
	npm install
	grunt server 

### You should be up and running now!

<http://localhost:3000/>

You will see some JSON returned to the service:  "Not Found" which means it's up and running!

