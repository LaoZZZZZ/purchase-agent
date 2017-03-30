#!/bin/bash
echo "activate user."

curl -i -X PUT -H "authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDMtMTlUMTg6NDI6MDUuNjc4WiIsImF1dGhfdGlja2V0IjoiMDIxM2MyZDEtNGQwMi00OGVjLWJjM2YtMjg2OWY2ZmYzYWZlIn0=" -d "" 'https://purchase-agent.appspot.com/user/activate/test?activationToken=94f44b63-8f8d-4c49-a9fc-8e7a3cd84c21'
