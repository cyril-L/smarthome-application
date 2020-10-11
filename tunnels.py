#!/usr/bin/env python3

import subprocess
import argparse
import sys

parser = argparse.ArgumentParser()
parser.add_argument("--postgres", help="Forward remote PostgreSQL to local",
                    action="store_true")
parser.add_argument("--rabbitmq", help="Forward  remote RabbitMQ to local",
                    action="store_true")
parser.add_argument("--app", help="Forward local web app to remote",
                    action="store_true")

parser.add_argument("--user", help="Remote user", default="clugan")
parser.add_argument("--host", help="Remote host", default="srv6.breizh-sen2.eu")
parser.add_argument("--port", help="SSH port", default="22000")

args = parser.parse_args()

tunnels = []

if args.postgres:
    tunnels.append(["-L", "5432:127.0.0.1:5432"])

if args.rabbitmq:
    tunnels.append(["-L", "5672:127.0.0.1:5672"])

if args.app:
    tunnels.append(["-R", "8000:127.0.0.1:8000"])

if not tunnels:
    parser.print_help()
    sys.exit(0)

processes = []

for tunnel in tunnels:
    cmd = ["ssh", "-p", args.port, "-N"] + tunnel + [f"{args.user}@{args.host}"]
    print(cmd)
    p = subprocess.Popen(cmd)
    processes.append(p)

input("Press Enter to stop forwards...")

for p in processes:
    p.terminate()
    p.wait()
