#!/bin/bash
echo "activate user."

curl -i -X PUT -H "authorization: eyJ1c2VybmFtZSI6Imx1emhhbyIsInBhc3N3b3JkIjoiU2FuZHlAMjAxMyIsImV4cGlyZV90aW1lIjoiMjAxNy0wNS0wOFQwOToxNTo0Ni4zODBaIiwiYXV0aF90aWNrZXQiOiIyMzZjMDg3Mi0xMmE4LTQ5YmMtOTZkNC1jNjk4MGY2NWQ4ODMifQ=" -d "" 'https://purchase-agent.appspot.com/user/activate/luzhao?activationToken=e3117e9a-38a6-43fb-b4ea-156125a2039f'
