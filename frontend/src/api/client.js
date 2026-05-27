const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
}

export async function request(path, options = {}) {
  const response = await fetch(path, {
    credentials: 'include',
    headers: {
      ...DEFAULT_HEADERS,
      ...(options.headers || {}),
    },
    ...options,
  })

  const payload = await response.json().catch(() => null)

  if (!response.ok) {
    throw new Error(payload?.message || `Request failed: ${response.status}`)
  }

  if (payload && typeof payload === 'object' && 'data' in payload) {
    return payload.data
  }

  return payload
}
