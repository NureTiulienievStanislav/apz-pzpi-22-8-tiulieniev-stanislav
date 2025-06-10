
import React from 'react';

export function Input({ type = 'text', ...props }) {
  return (
    <input
      type={type}
      className="w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring focus:ring-blue-200"
      {...props}
    />
  );
}
