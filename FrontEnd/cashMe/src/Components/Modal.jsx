import { useRef, useEffect } from "react";
import { createPortal } from "react-dom";

export default function Modal({ open, onClose, children }) {
  const ref = useRef(null);

  useEffect(() => {
    if (!ref.current) return;

    if (open) {
      if (!ref.current.open) {
        ref.current.showModal();
      }
    } else {
      if (ref.current.open) {
        ref.current.close();
      }
    }
  }, [open]);

  return createPortal(
    <dialog
      ref={ref}
      className="fixed inset-0 m-auto h-fit rounded-xl shadow-2xl p-0 border border-amber-900/30 bg-transparent backdrop:bg-black/70 backdrop:backdrop-blur-md"
      onCancel={(e) => {
        e.preventDefault(); 
        onClose(); 
      }}
      onClick={(e) => {
        if (e.target === ref.current) {
          onClose();
        }
      }}
    >
      <div className="bg-[#1a1a1a] rounded-xl min-w-[400px] max-w-[90vw] max-h-[90vh] overflow-auto">
        {children}
      </div>
    </dialog>,
    document.getElementById('modal-root')
  );
}