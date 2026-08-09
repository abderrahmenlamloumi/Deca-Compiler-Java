from flask_cors import CORS, cross_origin
import os
import random
import string
import time
from threading import Thread
from flask import Flask, request, send_from_directory

import socket
import subprocess


path = '/opt/gl/'
with open("./gitlab_token", 'r') as ft:
    token = ft.read().strip()

remote_url = f"https://oauth2:{token}@gitlab.ensimag.fr/glapp2025/gl10.git"
app = Flask(__name__, static_folder='../online_doc/build/', static_url_path='')
cors = CORS(app) # allow CORS for all domains on all routes.

app.config['CORS_HEADERS'] = 'Content-Type'

update_thread: Thread

def update():
    result = None
    try:
        app.logger.error("Updating repo in one minute...")
        #time.sleep(60)
        app.logger.error("Updating repo...")
        result = subprocess.run(['git', 'pull'], cwd=path, check=True, capture_output=True, text=True)
        app.logger.error(result.stdout)
        app.logger.error(result.stderr)
        app.logger.error("Compiling...")
        subprocess.Popen(['mvn', 'compile'], cwd=path)
        app.logger.error("Ready !")
    except subprocess.CalledProcessError as e:
        app.logger.error(e)
        app.logger.error(result.stdout)
        app.logger.error(result.stderr)

def init():
    try:
        app.logger.error("Starting Init")
        app.logger.error("Safe directory")
        app.logger.error(subprocess.check_output(["id"],stderr=subprocess.STDOUT))
        gitinit = subprocess.check_output(["git", "config", "--global", "--add", "safe.directory", path], cwd=path,stderr=subprocess.STDOUT)
        app.logger.error("Repo token")
        gitinit = subprocess.check_output(["git", "remote", "set-url", "origin", remote_url], cwd=path, stderr=subprocess.STDOUT)
        app.logger.error("Permissions")
        #gitinit = subprocess.check_output(["chmod", "+x", "/opt/gl/src/main/bin/*"], cwd=path, stderr=subprocess.STDOUT)
        #gitinit = subprocess.check_output(["chmod", "+x", "/opt/gl/src/test/script/*"], cwd=path, stderr=subprocess.STDOUT)
        app.logger.error("MVN")
        gitinit = subprocess.check_output(['mvn', 'dependency:resolve'], cwd=path, stderr=subprocess.STDOUT)
        gitinit = subprocess.check_output(['mvn', 'compile'], cwd=path, stderr=subprocess.STDOUT)
        app.logger.error("Init went ok")
    except subprocess.CalledProcessError as e:
        app.logger.error(e)
        app.logger.error(f"GIT INIT REPO URL,{gitinit} ")
        exit(1)


def update_listener():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    gitinit = ""


    server_socket.bind(('0.0.0.0', 5874))
    while True:

        try:
            data = server_socket.recv(128)
            if data.decode().lower().rstrip() == 'update':
                update()
                break
        except Exception as e:
            app.logger.error("A")
            app.logger.error(e)
        except KeyboardInterrupt as e:
            server_socket.close()


        server_socket.close()

#t = Thread(target=update_listener).start()
Thread(target=init).start()

@app.errorhandler(404)
def not_found(e):
    app.logger.error(f"[404] PATH: {request.path}")
    return send_from_directory(app.static_folder, "index.html")

@app.route("/", defaults={"path": ""})
@app.route("/<path:path>")
def serve_react_app(path):
    app.logger.error(f"PATH {path}")
    if path != "" and os.path.exists(os.path.join(app.static_folder, path)):
        return send_from_directory(app.static_folder, path)
    else:
        # Route SPA : index.html pour React Router
        return send_from_directory(app.static_folder, "index.html")



@app.route('/update')
@cross_origin()
def up():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_socket.connect(('localhost', 5874))
    server_socket.sendto("update".encode(), ('127.0.0.1', 5874))
    server_socket.close()
    return "Updating in one minute..."

@app.route('/compile', methods=['POST'])
@cross_origin()
def compile_deca():

    data = request.get_data().decode()

    file_name = ''.join(random.choices(string.ascii_uppercase + string.digits, k=8))
    deca_filepath = f"/tmp/{file_name}.deca"
    ass_filepath = f"/tmp/{file_name}.ass"

    with open(deca_filepath, "w") as f:
        app.logger.error(f"Writing {data} in {deca_filepath}")
        f.write(data)


    is_decac_done = False
    decac = ""
    try:
        decac = subprocess.check_output(f"{path}src/main/bin/decac -farray -fconcat-string -fassert {deca_filepath}", stderr=subprocess.STDOUT, shell=True)
        is_decac_done = True
        ima = subprocess.check_output(f"{path}global_bin/ima -t 9999 -p 9999 {ass_filepath}", stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        app.logger.error(e)
        if not is_decac_done:
            decac = e.stdout.decode()
            ima = None
        else:
            decac = None
            ima = e.stdout.decode()


    try:
        os.remove(deca_filepath)
        os.remove(ass_filepath)
    except FileNotFoundError:
        pass

    try:
        decac = decac.decode()
    except Exception:
        pass

    try:
        ima = ima.decode()
    except Exception:
        pass

    return {
        "decac" : decac,
        "ima": ima,
    }
