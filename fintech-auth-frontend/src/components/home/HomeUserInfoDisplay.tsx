import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export const UserInfoDisplay = async () => {
  const cookieStore = await cookies();

  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;

  const res = await fetch(`${backendHostname}/api/account/user-info`, {
    headers: {
      Cookies: cookieStore.toString(),
    },
    cache: "no-store",
  });

  if (!res.ok) {
    redirect("/auth/login");
  }

  const { userName, emailAddress } = await res.json();

  return (
    <div>
      <h1 className="text-4xl sm:text-5xl lg:text-6xl font-semibold leading-tight line-clamp-1">
        {userName}
      </h1>

      <p className="text-lg text-white/80">{emailAddress}</p>
    </div>
  );
};
