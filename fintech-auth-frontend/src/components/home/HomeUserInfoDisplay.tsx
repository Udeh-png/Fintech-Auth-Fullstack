export const UserInfoDisplay = async () => {
  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;
  // const { userName, emailAddress } = await fetch(
  //   `${backendHostname}/api/account/user-info`,
  // ).then(async (res) => await res.json());

  await new Promise((resolve) => {
    setTimeout(() => {
      resolve(null);
    }, 5000);
  });
  return (
    <div>
      <h1 className="text-4xl sm:text-5xl lg:text-6xl font-semibold leading-tight line-clamp-1">
        Udeh Chisom
      </h1>

      <p className="text-lg text-white/80">leonwokedichisom@gmail.com</p>
    </div>
  );
};
