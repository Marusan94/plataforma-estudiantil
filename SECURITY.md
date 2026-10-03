# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| main    | ✅ Yes             |
| develop | ✅ Yes             |
| < 1.0   | ❌ No              |

## Reporting a Vulnerability

We take security seriously. If you discover a security vulnerability, please report it responsibly.

### How to Report

**Preferred:** Use GitHub Security Advisories (private)
1. Go to the repository's **Security** tab
2. Click **Report a vulnerability**
3. Fill in the details

**Alternative:** Email security@educore.platform (if configured)

### What to Include

Please provide:
- **Description** of the vulnerability
- **Steps to reproduce** (minimal PoC)
- **Impact assessment** (what could an attacker do?)
- **Affected components** (backend, frontend, specific endpoints)
- **Suggested fix** (if you have one)

### Response Timeline

| Severity | Response Time | Resolution Target |
| -------- | ------------- | ----------------- |
| Critical | 24 hours      | 72 hours          |
| High     | 48 hours      | 1 week            |
| Medium   | 1 week        | 2 weeks           |
| Low      | 2 weeks       | Next release      |

## Security Measures in Place

### Backend (Spring Boot)
- Spring Security with JWT authentication
- CORS configuration (restricted origins)
- Input validation (Bean Validation + custom validators)
- SQL injection prevention (JPA/Hibernate parameter binding)
- Rate limiting (planned)
- Security headers (HSTS, CSP, X-Frame-Options)

### Frontend (React + Vite)
- Content Security Policy (CSP)
- XSS prevention (React auto-escaping)
- Dependency scanning (npm audit + GitHub Dependabot)
- No eval() or dangerouslySetInnerHTML in production code

### Data Analysis (Python)
- No pickle/unpickle of untrusted data
- Input validation on CSV/JSON inputs
- Sandboxed execution environment

### Infrastructure
- HTTPS enforced (Render TLS)
- Environment variables for secrets (no hardcoded secrets)
- Database: H2 in-memory (dev), PostgreSQL (prod) with SSL
- GitHub Actions: minimal permissions, OIDC for cloud deploys

## Dependency Management

- **Dependabot** enabled for: Maven (Java), npm (Node), pip (Python)
- **Auto-merge** for patch/minor updates with passing CI
- **Manual review** for major updates
- **Trivy** scanning in CI for container/fs vulnerabilities

## Disclosure Policy

We follow **Coordinated Vulnerability Disclosure**:
1. Reporter submits vulnerability privately
2. We acknowledge within 24h (critical) / 48h (high)
3. We investigate and develop fix
4. We coordinate disclosure timeline with reporter
5. Public advisory published after fix is deployed

## Hall of Fame

Thank you to security researchers who have helped improve EDU.CORE:
<!-- Contributors will be listed here -->

---
*Last updated: 2026-10-03*