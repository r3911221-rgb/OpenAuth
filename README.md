# OpenAuth

[![Build Status](https://github.com/OpenAuth/OpenAuth/actions/workflows/build.yml/badge.svg)](https://github.com/OpenAuth/OpenAuth/actions)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

A highly secure, privacy-focused, open-source two-factor authentication (2FA) application for Android.

OpenAuth generates authentication codes completely offline on your device. Your secrets remain encrypted locally and are never uploaded without your permission.

## Core Features

### Offline First

- Generate TOTP codes offline
- HOTP support
- Steam Guard support
- No account registration required
- No cloud dependency

### Secure Storage

- SQLCipher encrypted database
- AES-256-GCM encryption
- Android Keystore integration
- Hardware-backed key protection
- Encrypted backups
- Secure key derivation using Argon2id, Scrypt, and PBKDF2

### Privacy & Security

- Biometric authentication
- PIN protection
- Screenshot prevention
- Root detection support
- Secure memory handling
- Constant-time cryptographic comparison
- Minimal permissions
- No tracking

### Import & Export

Supports migration from:

- Aegis Authenticator
- Authy
- Google Authenticator
- 2FAS
- Standard otpauth:// QR codes

### Organization & Customization

- Tags and folders
- Account search
- Brand icons
- Custom themes
- Dynamic Material design

## Technology Stack

Built using modern Android development standards:

- Kotlin
- Jetpack Compose
- Material 3
- Dagger Hilt
- Room Database
- SQLCipher
- Android Keystore
- CameraX
- ML Kit Barcode Scanner
- Kotlin Coroutines & Flow

## Architecture

OpenAuth follows Clean Architecture principles: