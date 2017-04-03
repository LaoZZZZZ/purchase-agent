#!/bin/bash
echo "create single transaction."

curl -i -X POST -H "Content-Type: application/json" -H "authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDQtMDNUMjI6MTA6MjguNjEyWiIsImF1dGhfdGlja2V0IjoiMmU2YzRjNGQtMDhiOS00MjEwLWFlMzItY2RkOWY4OTI0NjkwIn0=" -d '{"status":"PAID", "transaction_id":"","customer_id":"123", "money_amount":{"amount":"200.00"},"line_item_ids":["item_1","item_2"]}' 'https://purchase-agent.appspot.com/transactions'

