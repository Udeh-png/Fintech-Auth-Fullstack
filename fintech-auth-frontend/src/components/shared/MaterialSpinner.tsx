export const MaterialSpinner = ({
  sizeInPx,
  color,
}: {
  sizeInPx: number;
  color?: "primary" | null;
}) => {
  const dynamicStyle = {
    width: `${sizeInPx}px`,
    height: `${sizeInPx}px`,
    "--width": `${sizeInPx}`,
  };
  return (
    <svg className="material-spinner overflow-visible" style={dynamicStyle}>
      <circle
        className="path"
        cx="50%"
        cy="50%"
        r="50%"
        fill="none"
        stroke={color == "primary" ? "var(--primary-color)" : "white"}
        strokeWidth={Math.round(sizeInPx * (10 / 100))}
      ></circle>
    </svg>
  );
};
