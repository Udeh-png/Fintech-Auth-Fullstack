import { Config } from "@/config";
import { cookies } from "next/headers";

export const UserInfoDisplay = async () => {
  const cookieStore = await cookies();

  const backendHostname = Config.API_URL;

  const res = await fetch(`${backendHostname}/api/account/user-info`, {
    headers: {
      Cookie: cookieStore.toString(),
    },
    cache: "no-store",
  });

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
