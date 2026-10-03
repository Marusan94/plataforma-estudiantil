import React from 'react';

export default function UniversityLogo({ size = 36, color = 'currentColor' }) {
  return (
    <svg 
      width={size} 
      height={size} 
      viewBox="0 0 48 48" 
      fill="none" 
      xmlns="http://www.w3.org/2000/svg"
      style={{ flexShrink: 0 }}
    >
      {/* Outer Shield */}
      <path 
        d="M24 4L8 10V22C8 31.6 14.8 40.5 24 44C33.2 40.5 40 31.6 40 22V10L24 4Z" 
        stroke={color} 
        strokeWidth="2.2" 
        strokeLinecap="round" 
        strokeLinejoin="round" 
      />
      {/* Inner Shield Inset */}
      <path 
        d="M24 8L12 12.8V21.5C12 29.2 17.1 36.3 24 39.5C30.9 36.3 36 29.2 36 21.5V12.8L24 8Z" 
        stroke={color} 
        strokeWidth="1.2" 
        strokeOpacity="0.5" 
        strokeLinecap="round" 
        strokeLinejoin="round" 
      />
      {/* Open Book of Knowledge */}
      <path 
        d="M17 28C19 26.5 21.5 26.5 24 28C26.5 26.5 29 26.5 31 28V20C29 18.5 26.5 18.5 24 20C21.5 18.5 19 18.5 17 20V28Z" 
        fill={color} 
        fillOpacity="0.2"
        stroke={color} 
        strokeWidth="1.6" 
        strokeLinejoin="round" 
      />
      <line x1="24" y1="20" x2="24" y2="28" stroke={color} strokeWidth="1.6" />
      {/* Torch of Enlightenment */}
      <path 
        d="M24 13V17" 
        stroke={color} 
        strokeWidth="2" 
        strokeLinecap="round" 
      />
      <path 
        d="M22 13C22 11.5 23 10.5 24 10C25 10.5 26 11.5 26 13C26 14 25 15 24 15C23 15 22 14 22 13Z" 
        fill={color} 
      />
      {/* Laurel Stars */}
      <circle cx="16.5" cy="16" r="1.2" fill={color} />
      <circle cx="31.5" cy="16" r="1.2" fill={color} />
    </svg>
  );
}
