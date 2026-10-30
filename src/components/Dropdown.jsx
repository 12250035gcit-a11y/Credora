import { useEffect, useRef, useState } from 'react'

function useClickOutside(ref, onOutside) {
  useEffect(() => {
    function handle(e) {
      if (ref.current && !ref.current.contains(e.target)) onOutside()
    }
    document.addEventListener('mousedown', handle)
    return () => document.removeEventListener('mousedown', handle)
  }, [ref, onOutside])
}

/** Single-select dropdown with a trailing "Other" option that reveals a text input. */
export function DropdownSelect({ options, value, isOther, placeholder, onSelect, onOtherText }) {
  const [open, setOpen] = useState(false)
  const ref = useRef(null)
  useClickOutside(ref, () => setOpen(false))

  const label = isOther ? (value || 'Other') : (value || placeholder)

  return (
    <div className="dropdown" ref={ref}>
      <button type="button" className="dropdown-toggle" onClick={() => setOpen((o) => !o)}>
        <span className="dropdown-toggle-text">{label}</span>
        <span className={`dropdown-chevron${open ? ' open' : ''}`}>▾</span>
      </button>
      {open && (
        <div className="dropdown-panel">
          {options.map((opt) => (
            <div
              key={opt}
              role="option"
              aria-selected={!isOther && value === opt}
              tabIndex={0}
              className={`dropdown-item${!isOther && value === opt ? ' selected' : ''}`}
              onClick={() => { onSelect(opt); setOpen(false) }}
              onKeyDown={(e) => (e.key === 'Enter' || e.key === ' ') && (onSelect(opt), setOpen(false))}
            >
              {opt}
            </div>
          ))}
          <div
            role="option"
            aria-selected={isOther}
            tabIndex={0}
            className={`dropdown-item${isOther ? ' selected' : ''}`}
            onClick={() => { onSelect('Other'); setOpen(false) }}
            onKeyDown={(e) => (e.key === 'Enter' || e.key === ' ') && (onSelect('Other'), setOpen(false))}
          >
            Other
          </div>
        </div>
      )}
      {isOther && (
        <input value={value} onChange={onOtherText} placeholder="Type to specify" autoFocus />
      )}
    </div>
  )
}

/** Multi-select dropdown (checkboxes) with an "Other" free-text row for extra, custom values. */
export function DropdownMultiSelect({ options, selected, placeholder, onToggle, customValue, onCustomChange }) {
  const [open, setOpen] = useState(false)
  const ref = useRef(null)
  useClickOutside(ref, () => setOpen(false))

  const summary = selected.length > 0 ? selected.join(', ') : placeholder

  return (
    <div className="dropdown" ref={ref}>
      <button type="button" className="dropdown-toggle" onClick={() => setOpen((o) => !o)}>
        <span className="dropdown-toggle-text">{summary}</span>
        <span className={`dropdown-chevron${open ? ' open' : ''}`}>▾</span>
      </button>
      {open && (
        <div className="dropdown-panel">
          {options.map((opt) => (
            <label key={opt} className="dropdown-checkbox-item">
              <input type="checkbox" checked={selected.includes(opt)} onChange={() => onToggle(opt)} />
              {opt}
            </label>
          ))}
          <div className="dropdown-divider" />
          <label className="dropdown-other-item">
            Other (comma separated)
            <input value={customValue} onChange={onCustomChange} placeholder="Type to add" />
          </label>
        </div>
      )}
    </div>
  )
}
