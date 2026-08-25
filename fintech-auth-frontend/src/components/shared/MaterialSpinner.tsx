export const MaterialSpinner = ({ sizeInPx }: { sizeInPx: number }) => {
  const dynamicStyle = {
    width: `${sizeInPx}px`,
    height: `${sizeInPx}px`,
    "--width": `${sizeInPx}`,
  };

  console.log(sizeInPx);
  return (
    <svg className="material-spinner overflow-visible" style={dynamicStyle}>
      <circle className="path" cx="50%" cy="50%" r="50%" fill="none"></circle>
    </svg>
  );
};
