# Spring SSO

## Spring Auth Server
- Spring auth server supports various grant types:
  1. Authorization Code
  2. Authorization Code + PKCE
  3. Client Credentials
  4. Refresh Token
  5. Implicit (Deprecated)
  6. Password (Deprecated)

## Spring Resource Server


## Terminology
- <b>Grant Type</b> - It is the way how client receives the token 
- <b>Auth Server</b> - Server where client and user details are stored and provides signed token to the requesters
- <b>Resource Server</b> - Server where resources are stored and client needs permission to access them
- <b>Scope - Scope is associated w/ client. It represents what client can do
- <b>Authority</b> - Authority is associated w/ user. It represents what users can do. e.g. Read, Write
- <b>Role</b> - Role is associated w/ user. It represents what users can do. e.g. Manager, Admin
- <b>Opaque Token</b> - Tokens which do not contain any data. 
- <b>Non-opaque Token</b> - Tokens which are self-contained, which has data that represents the client (Be it a service / user)
- <b>JWT</b> - Json Web Token. Mostly used non-opaque token