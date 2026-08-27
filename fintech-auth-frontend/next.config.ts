import type { NextConfig } from "next";

const origin = process.env.ALLOW_ORIGIN_IP || "";
const nextConfig: NextConfig = {
  /* config options here */
  reactCompiler: true,
  allowedDevOrigins: [origin],
};

export default nextConfig;
