import { FaCheckCircle, FaRegCircle } from "react-icons/fa";

export const PasswordListItem = ({
  criteria,
  isValid,
}: {
  criteria: { id: string; label: string };
  isValid: boolean;
}) => {
  return (
    <p
      className={`flex gap-2 items-center text-sm ${isValid ? "text-green-500" : "text-gray-500"}`}
    >
      {isValid ? (
        <FaCheckCircle className="text-xs check-mark-spin-in" />
      ) : (
        <FaRegCircle className="text-xs" />
      )}
      {criteria.label}
    </p>
  );
};
