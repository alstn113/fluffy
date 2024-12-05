import { Input, Textarea } from '@nextui-org/react';
import { useState } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { HiCheck } from 'react-icons/hi';
import { HiOutlineX } from 'react-icons/hi';

interface EditableInputProps {
  initialValue: string;
  onSave: (value: string) => void;
  label?: string;
  placeholder?: string;
  isTextarea?: boolean;
  maxLength?: number;
}

/**
 * onClick보다 onBlur가 먼저 발생하는 문제가 있음.
 * 이를 해결하기 위해 onMouseDown 이벤트를 사용하여 preventDefault를 호출하여 해결함.
 * 그러나 NextUI의 Button은 onPress, onClick이 먼저 작동하지 않았음.
 * 그래서 Button을 사용하지 않고 일반 button을 사용하여 해결함.
 */
const EditableInput = ({
  initialValue,
  onSave,
  label,
  placeholder,
  isTextarea,
  maxLength,
}: EditableInputProps) => {
  const [isEditing, setIsEditing] = useState(false);
  const [value, setValue] = useState(initialValue);

  const handleFocus = () => {
    if (isEditing) return;

    setIsEditing(true);
  };

  const handleCancel = () => {
    if (!isEditing) return;

    setIsEditing(false);
    setValue(initialValue);
  };

  const handleSave = () => {
    if (!isEditing) return;

    setIsEditing(false);
    onSave(value);
  };

  return (
    <div className="relative w-full" onFocus={handleFocus} onBlur={handleCancel}>
      {isTextarea ? (
        <Textarea
          size="lg"
          label={label}
          value={value}
          onValueChange={setValue}
          isReadOnly={!isEditing}
          placeholder={placeholder}
          variant="faded"
          minRows={3}
          maxLength={maxLength}
        />
      ) : (
        <Input
          size="lg"
          label={label}
          value={value}
          onValueChange={setValue}
          isReadOnly={!isEditing}
          placeholder={placeholder}
          fullWidth
          variant="faded"
          maxLength={maxLength}
        />
      )}
      <AnimatePresence>
        {isEditing && (
          <motion.div
            initial={{ opacity: 0, y: -5 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -5 }}
            className="flex gap-2 mt-2 w-full justify-end items-center absolute right-0"
          >
            <button
              onMouseDown={(e) => {
                e.preventDefault();
              }}
              onClick={handleSave}
              className="flex items-center justify-center rounded-lg bg-success text-white w-8 h-8 shadow-md hover:shadow-lg transition-shadow"
            >
              <HiCheck size={20} />
            </button>
            <button
              onMouseDown={(e) => {
                e.preventDefault();
              }}
              onClick={handleCancel}
              className="flex items-center justify-center rounded-lg bg-danger text-white w-8 h-8 shadow-md hover:shadow-lg transition-shadow"
            >
              <HiOutlineX size={20} />
            </button>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default EditableInput;
