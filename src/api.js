export const API_BASE = 'http://localhost:8080'

async function request(path, { method = 'GET', token, body } = {}) {
  let res
  try {
    res = await fetch(`${API_BASE}${path}`, {
      method,
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: body !== undefined ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new Error('Could not reach the server. Is the backend running?')
  }

  const data = await res.json().catch(() => ({}))
  if (!res.ok) {
    throw new Error(data.message || 'Something went wrong. Please try again.')
  }
  return data
}

export const apiGet = (path, token) => request(path, { token })
export const apiPut = (path, token, body) => request(path, { method: 'PUT', token, body })
export const apiPost = (path, token, body) => request(path, { method: 'POST', token, body })
