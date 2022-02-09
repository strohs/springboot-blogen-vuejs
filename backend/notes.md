## How to generate RSA public and private key pair in PKCS#8 format

This key to achieving this is basically a three step process:

1. Create key pair

>openssl genrsa -out keypair.pem 2048

2. Extract public part

>openssl rsa -in keypair.pem -pubout -out publickey.crt

At this point you have your public key called publickey.crt



3. Extract private part

>openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out pkcs8.key

At this point you have your private key and it’s called pkcs8.key


