#!/usr/local/bin/python3

import os
import shutil

while not os.getcwd().lower().endswith("backend"):
    os.chdir("..")

shutil.rmtree("ServerHttp/src/main/resources/assets/static/js")
os.system("cp -rf ../../JSProjects/raspberry-wallet-frontend/build/ ServerHttp/src/main/resources/assets/")
os.system("git add ServerHttp/src/main/resources/assets")
