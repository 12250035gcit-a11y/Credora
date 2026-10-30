import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useCv } from '../CvContext'

const API_BASE = 'http://localhost:8080'

const PASSWORD_HINT = 'At least 8 characters, with an uppercase letter, a lowercase letter, a number, and a special character.'

function isStrongPassword(value) {
  return value.length >= 8
    && /[a-z]/.test(value)
    && /[A-Z]/.test(value)
    && /\d/.test(value)
    && /[^A-Za-z0-9]/.test(value)
}

const eyeCommon = { width: 18, height: 18, viewBox: '0 0 20 20', fill: 'none', stroke: 'currentColor', strokeWidth: 1.6, strokeLinecap: 'round', strokeLinejoin: 'round' }

function EyeIcon() {
  return (
    <svg {...eyeCommon}>
      <path d="M1.5 10S4.5 4 10 4s8.5 6 8.5 6-3 6-8.5 6-8.5-6-8.5-6z" />
      <circle cx="10" cy="10" r="2.5" />
    </svg>
  )
}

function EyeOffIcon() {
  return (
    <svg {...eyeCommon}>
      <path d="M1.5 10S4.5 4 10 4s8.5 6 8.5 6-3 6-8.5 6-8.5-6-8.5-6z" />
      <circle cx="10" cy="10" r="2.5" />
      <path d="M2.5 2.5l15 15" />
    </svg>
  )
}

export default function Login() {
  const [mode, setMode] = useState('login')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [error, setError] = useState('')
  const { login } = useCv()
  const navigate = useNavigate()

  function switchMode() {
    setMode(mode === 'login' ? 'signup' : 'login')
    setError('')
    setConfirmPassword('')
  }

  async function handleSubmit(e) {
    e.preventDefault()

    if (!email.trim() || !email.includes('@') || !password.trim()) {
      setError('Wrong information. Please check your email and password.')
      return
    }

    if (mode === 'signup') {
      if (!isStrongPassword(password)) {
        setError(`Password is too weak. ${PASSWORD_HINT}`)
        return
      }
      if (password !== confirmPassword) {
        setError('Passwords do not match.')
        return
      }
    }

    setError('')

    const endpoint = mode === 'signup' ? '/api/auth/signup' : '/api/auth/login'
    const body = { email: email.trim(), password }

    try {
      const res = await fetch(`${API_BASE}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      })
      const data = await res.json()

      if (!res.ok) {
        setError(data.message || 'Something went wrong. Please try again.')
        return
      }

      login(data.email, data.token)
      navigate('/choice')
    } catch {
      setError('Could not reach the server. Is the backend running?')
    }
  }

  return (
    <div className="page centered">
      <div className="card auth-card">
        <h1>Credora</h1>
        <p className="subtitle">{mode === 'login' ? 'Log in to your account' : 'Create a new account'}</p>

        <form onSubmit={handleSubmit} className="form" noValidate>
          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="jamtshokayakay13@gmail.com"
            />
          </label>

          <label>
            Password
            <div className="password-field">
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? <EyeOffIcon /> : <EyeIcon />}
              </button>
            </div>
            {mode === 'signup' && <p className="field-hint">{PASSWORD_HINT}</p>}
          </label>

          {mode === 'signup' && (
            <label>
              Re-enter Password
              <div className="password-field">
                <input
                  type={showConfirmPassword ? 'text' : 'password'}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="••••••••"
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  aria-label={showConfirmPassword ? 'Hide password' : 'Show password'}
                >
                  {showConfirmPassword ? <EyeOffIcon /> : <EyeIcon />}
                </button>
              </div>
            </label>
          )}

          {error && <p className="error">{error}</p>}

          <button type="submit" className="btn btn-primary">
            {mode === 'login' ? 'Log In' : 'Create Account'}
          </button>
        </form>

        <button type="button" className="btn-link" onClick={switchMode}>
          {mode === 'login' ? "Don't have an account? Sign up" : 'Already have an account? Log in'}
        </button>
      </div>
    </div>
  )
}
