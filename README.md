# 🔐 OpenAuth Authenticator

**Free, Secure, Open Source Two-Factor Authentication for Android**

[![License](https://img.shields.io/badge/License-OpenAuth-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-purple.svg)](https://kotlinlang.org)
[![Security Rating](https://img.shields.io/badge/Security-9.2%2F10-success.svg)](SECURITY.md)

OpenAuth is a production-grade authenticator app that generates secure one-time passwords for your online accounts. Built with security-first architecture and zero telemetry.

---

## ✨ Features

### Core Authentication
- ✅ **TOTP** — RFC 6238 compliant time-based codes
- ✅ **HOTP** — RFC 4226 compliant counter-based codes
- ✅ **Steam Guard** — Valve's custom 5-character alphabet
- ✅ **SHA-1 / SHA-256 / SHA-512** — Full algorithm support
- ✅ **6, 7, 8 digit codes** — Flexible digit lengths
- ✅ **Custom periods** — 30s, 60s, or any 1-300s interval

### Security Architecture
- 🔒 **AES-256-GCM** — All secrets encrypted at rest
- 🔒 **Argon2id** — Memory-hard key derivation (64MB, 3 iterations)
- 🔒 **Scrypt** — Alternative KDF (N=32768, r=8, p=1)
- 🔒 **PBKDF2-HMAC-SHA256** — Legacy KDF (600,000 iterations)
- 🔒 **Android Keystore** — Hardware-backed keys with StrongBox
- 🔒 **Biometric Lock** — Class 3 (Strong) biometric authentication
- 🔒 **SQLCipher** — Encrypted database (AES-256-CBC)
- 🔒 **Memory Wiping** — All cryptographic material wiped after use
- 🔒 **Anti-Tamper** — Root, emulator, and debugger detection

### Privacy & Protection
- 🛡️ **Zero Telemetry** — No analytics, no tracking, no crash reporters
- 🛡️ **Offline First** — No internet permission required
- 🛡️ **Screenshot Blocking** — FLAG_SECURE on all sensitive screens
- 🛡️ **Clipboard Safety** — IS_SENSITIVE flag + auto-clear (30s)
- 🛡️ **Duress PIN** — Silent panic trigger with decoy vault
- 🛡️ **Lockout Policy** — Exponential backoff (5s → 30min max)

### User Experience
- 🎨 **Material 3 Design** — Jetpack Compose UI
- 🌙 **Dark Mode** — System, Light, Dark, AMOLED themes
- 🔍 **Search** — Instant account search with debounce
- 📱 **Widget** — Home screen widget for quick access
- 🔗 **Quick Settings Tile** — Direct access from notification shade
- 🌍 **41 Languages** — Full internationalization support

### Backup & Import
- 📥 **QR Code Import** — Scan otpauth:// URIs
- 📤 **QR Code Export** — Share accounts securely
- 📂 **Encrypted Backup** — AES-256-GCM encrypted vault export
- 🔄 **Multi-Format Import** — Support for 9 authenticator apps:
  - Google Authenticator
  - Microsoft Authenticator
  - Authy
  - Bitwarden
  - Aegis
  - andOTP
  - FreeOTP / FreeOTP+
  - WinAuth
  - 2FAS

---

## 🏗️ Architecture

OpenAuth follows **Clean Architecture** with a single `app/` module:
