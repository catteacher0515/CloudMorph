import { request } from './client'

export function fetchDashboardOverview() {
  return request('/api/dashboard/overview')
}
