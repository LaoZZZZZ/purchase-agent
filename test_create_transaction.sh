#!/bin/bash

curl -X POST -H 'Content-type: application/json' -d '@transaction.json' https://purchase-agent.appspot.com/transactions -H 'authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDQtMjFUMDQ6NDA6MTMuODc3WiIsImF1dGhfdGlja2V0IjoiOGU5MDY1NzktYTVlZi00MGFlLThkNWUtZWQ4NTFhNjhmMDkyIn0='  
