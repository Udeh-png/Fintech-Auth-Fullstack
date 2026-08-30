# Fintech Auth

A standalone authentication and account service built as part of the [Walletly](https://github.com/Udeh-png/Walletly-Fullstack.git) fintech project. This repo demonstrates a production-style auth flow — JWT httpOnly cookies, email OTP verification, and cross-subdomain session handling — deployed as a live, working system rather than just local demo code.

> **Note:** The main Walletly project is yet to be deployed. This project focuses specifically on the auth architecture and is deployed publicly for demonstration.

## Live Demo

- Frontend: `https://app.fintechauth.site`
- Backend API: `https://api.fintechauth.site`

## Tech Stack

**Frontend**

- Next.js (App Router)
- Server Components with server-side authenticated data fetching

**Backend**

- Spring Boot
- Spring Security
- MongoDB Atlas
- JWT (httpOnly cookie-based auth)
- [Resend](https://resend.com) for transactional email (OTP delivery)

**Infrastructure**

- Backend hosted on Railway (`api.fintechauth.site`)
- Frontend hosted on Vercel (`app.fintechauth.site`)
- Custom domain via Hostinger, with subdomains split across services

## Architecture Notes

- **Auth cookies** are httpOnly, `Secure`, and scoped to the root domain (`.fintechauth.site`), allowing the frontend and backend subdomains to share session state as same-site requests — avoiding the fragility of `SameSite=None` cross-site cookies.
- **OTP emails** are sent via Resend using a verified sending subdomain (`mail.fintechauth.site`), rather than raw SMTP — chosen after discovering Railway blocks outbound SMTP ports (465/587) on non-Pro plans.
- **Server-side data fetching**: the homepage fetches user/wallet info directly from a Next.js Server Component, manually forwarding the incoming request's cookies to the backend (since server-side `fetch` calls don't automatically carry browser cookies the way client-side requests do).

## Known Limitation:

OTP emails may land in the recipient's span folder due to the sending domain's limited sending history (SPF/DKIM configured; DMARC and reputation warm-up still in progress)

## Getting Started

### Backend

```bash
cd fintech-auth-backend
./mvnw spring-boot:run
```

Required environment variables:

```
spring.mongodb.uri=<your MongoDB Atlas connection string>
RESEND_API_KEY=<your Resend API key>
RESEND_FROM_EMAIL=<verified sending address>
```

### Frontend

```bash
cd fintech-auth-frontend
npm install
npm run dev
```

Required environment variables:

```
NEXT_PUBLIC_API_URL=<backend URL>
```

## Related

- [Walletly](https://github.com/Udeh-png/Walletly-Fullstack.git)
