#!/bin/bash

curl --request POST \
--url https://purchase-agent.appspot.com//transactions \
--header 'authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDQtMDNUMjI6MTA6MjguNjEyWiIsImF1dGhfdGlja2V0IjoiMmU2YzRjNGQtMDhiOS00MjEwLWFlMzItY2RkOWY4OTI0NjkwIn0=' \
--header 'cache-control: no-cache' \
--header 'content-category: application/json' \
--header 'postman-token: 5488e22d-ed58-5a97-01f0-8e5ab6a9565e' \
--data '{"status":"PAID","money_amount":"{'\''amount'\'':'\''200.00'\''}","line_item_ids":"['\''item_1'\'']","customer_id":"customer_1"}'