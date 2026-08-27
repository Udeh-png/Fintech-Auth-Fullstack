import { Drawer } from "vaul";

export const BottomSheet = ({
  isOpen,
  onClose,
  children,
}: {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
}) => {
  return (
    <Drawer.Root open={isOpen} onClose={onClose}>
      <Drawer.Portal>
        <Drawer.Content className="fixed z-100 rounded-t-3xl h-fit bottom-0! mb-0! left-0 w-full bg-[#161224] py-3 px-3 overflow-hidden">
          <Drawer.Handle />
          {children}
        </Drawer.Content>
      </Drawer.Portal>
    </Drawer.Root>
  );
};
