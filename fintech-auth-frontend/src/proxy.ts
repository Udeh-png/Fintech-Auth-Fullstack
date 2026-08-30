import { NextRequest, NextResponse } from "next/server";

export default function proxy(request: NextRequest) {
  const accessToken = request.cookies.get("ACCESS_TOKEN");
  const refreshToken = request.cookies.get("REFRESH_TOKEN");
  const regId = request.cookies.get("REGISTRATION_SESSION_ID");
  const forgotPasswordId = request.cookies.get("FORGOT_PASSWORD_SESSION_ID");
  const resetPasswordId = request.cookies.get("RESET_PASSWORD_SESSION_ID");

  // if (request.nextUrl.pathname == "/home") {
  //   if (accessToken || refreshToken) return NextResponse.next();

  //   const redirectUrl = request.nextUrl.clone();
  //   redirectUrl.pathname = "/auth/login";
  //   return NextResponse.redirect(redirectUrl);
  // }

  if (request.nextUrl.pathname == "/auth/login") {
    if (!accessToken || !refreshToken) return NextResponse.next();

    const redirectUrl = request.nextUrl.clone();
    redirectUrl.pathname = "/home";
    return NextResponse.redirect(redirectUrl);
  }

  if (request.nextUrl.pathname == "/auth/email-verification") {
    if (regId || resetPasswordId || forgotPasswordId)
      return NextResponse.next();

    const redirectUrl = request.nextUrl.clone();
    redirectUrl.pathname = "/auth/login";
    return NextResponse.redirect(redirectUrl);
  }
}
