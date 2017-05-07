#!/bin/bash
echo "trigger cron job."

curl -L -X GET -H 'authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDUtMDhUMDE6MTY6MDIuMTA2WiIsImF1dGhfdGlja2V0IjoiZGM3ZTg0MWMtOWZjYS00MDNhLWFiMWUtNWRkZDA3ZmNiNTE3In0=' 'https://purchase-agent.appspot.com/cron/transaction/summary'

