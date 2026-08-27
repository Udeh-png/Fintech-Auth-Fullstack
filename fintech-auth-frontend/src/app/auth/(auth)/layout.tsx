import { AuthLeftSide } from "@/components/auth/AuthLeftSide";

export default function Layout({
  children,
  AuthPages,
}: {
  children: React.ReactNode;
  AuthPages: React.ReactNode;
}) {
  return (
    <div className="md:px-10 md:pt-7 pt-5">
      <div className="fixed size-120 blur-3xl rounded-full md:-bottom-50 -top-50 -left-30 bg-primary -z-10 opacity-10" />

      <div className="grid lg:grid-cols-2 mx-auto gap-x-10 gap-y-7">
        {children}

        <div className="pr-1">
          <AuthLeftSide />
        </div>

        <div className="flex items-center justify-center">{AuthPages}</div>
      </div>

      <div className="fixed size-120 blur-3xl rounded-full md:-bottom-50 -bottom-100 -right-10 bg-primary -z-10 opacity-10" />
    </div>
  );
}
