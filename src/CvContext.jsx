import { createContext, useCallback, useContext, useEffect, useState } from 'react'
import { apiGet, apiPost, apiPut } from './api'

const CvContext = createContext(null)
const AUTH_STORAGE_KEY = 'credora.auth'

const emptyCv = {
  name: '',
  photo: null,
  jobTitle: '',
  email: '',
  phone: '',
  location: '',
  linkedin: '',
  portfolio: '',
  summary: '',
  education: '',
  experience: '',
  projects: '',
  skills: '',
  languages: '',
  certificates: '',
  certificateImages: [],
}

function readStoredAuth() {
  try {
    const raw = localStorage.getItem(AUTH_STORAGE_KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

// Backend stores certificate images as plain base64 strings; the frontend
// keeps {id, name, dataUrl} so it can render a remove button per image.
function imagesFromServer(images) {
  return (images ?? []).map((dataUrl, i) => ({ id: `server-${i}`, name: `Certificate ${i + 1}`, dataUrl }))
}

function imagesToServer(images) {
  return images.map((img) => img.dataUrl)
}

function cvFromResponse(data) {
  return {
    name: data.name ?? '',
    photo: data.photo ?? null,
    jobTitle: data.jobTitle ?? '',
    email: data.email ?? '',
    phone: data.phone ?? '',
    location: data.location ?? '',
    linkedin: data.linkedin ?? '',
    portfolio: data.portfolio ?? '',
    summary: data.summary ?? '',
    education: data.education ?? '',
    experience: data.experience ?? '',
    projects: data.projects ?? '',
    skills: data.skills ?? '',
    languages: data.languages ?? '',
    certificates: data.certificates ?? '',
    certificateImages: imagesFromServer(data.certificateImages),
  }
}

export function CvProvider({ children }) {
  const [user, setUser] = useState(() => readStoredAuth())
  const [template, setTemplate] = useState('modern')
  const [cv, setCv] = useState(emptyCv)
  const [cvLoading, setCvLoading] = useState(false)

  const applyCvResponse = useCallback((data) => {
    setTemplate(data.template || 'modern')
    setCv(cvFromResponse(data))
  }, [])

  const loadCv = useCallback(async (token) => {
    setCvLoading(true)
    try {
      const data = await apiGet('/api/cv', token)
      applyCvResponse(data)
    } catch {
      // Token is stale (e.g. backend restarted since last visit) - log out quietly.
      setUser(null)
      localStorage.removeItem(AUTH_STORAGE_KEY)
    } finally {
      setCvLoading(false)
    }
  }, [applyCvResponse])

  // Rehydrate a saved session once on mount, so a page refresh doesn't log the user out.
  useEffect(() => {
    if (user?.token) loadCv(user.token)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const login = useCallback(async (email, token) => {
    setUser({ email, token })
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify({ email, token }))
    await loadCv(token)
  }, [loadCv])

  const logout = useCallback(() => {
    setUser(null)
    localStorage.removeItem(AUTH_STORAGE_KEY)
    setTemplate('modern')
    setCv(emptyCv)
  }, [])

  const saveCv = useCallback(async () => {
    if (!user) throw new Error('Not logged in.')
    const data = await apiPut('/api/cv', user.token, {
      template,
      ...cv,
      certificateImages: imagesToServer(cv.certificateImages),
    })
    applyCvResponse(data)
    return data
  }, [user, template, cv, applyCvResponse])

  const undoCv = useCallback(async () => {
    if (!user) throw new Error('Not logged in.')
    const data = await apiPost('/api/cv/undo', user.token)
    applyCvResponse(data)
    return data
  }, [user, applyCvResponse])

  const value = { user, login, logout, template, setTemplate, cv, setCv, cvLoading, saveCv, undoCv }
  return <CvContext.Provider value={value}>{children}</CvContext.Provider>
}

export function useCv() {
  const ctx = useContext(CvContext)
  if (!ctx) throw new Error('useCv must be used within CvProvider')
  return ctx
}
