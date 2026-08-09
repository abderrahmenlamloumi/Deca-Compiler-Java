import os
import subprocess

path = '/opt/gl/'
deca_filepath = "/tmp/0N68ZH17.deca"

env = os.environ.copy()
try:

    print(f"Fichier cible : {deca_filepath}")
    print(f"Chemin absolu : {os.path.abspath(deca_filepath)}")
    print(f"Existe ? {os.path.exists(deca_filepath)}")
    print(f"Taille : {os.path.getsize(deca_filepath) if os.path.exists(deca_filepath) else 'n/a'}")

    with open(deca_filepath, 'r') as f:
        content = f.read()
        print("Contenu du fichier :", content[:300])

    output = subprocess.check_output(
        f"{path}src/main/bin/decac {deca_filepath}",
        stderr=subprocess.STDOUT,
        shell=True,
    )
    print(output)
except subprocess.CalledProcessError as e:
    print(e)
    print(e.stdout)


