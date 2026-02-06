# BrowserActionsUtility
# Selenium BrowserActions Utility 🚀

A robust, production-ready Selenium Java utility wrapper designed to enhance test stability, handle dynamic web elements, and simplify browser interactions.

---

## 📌 Overview
The **BrowserActions** class acts as the **Intelligence Layer** of a Selenium automation framework. It abstracts complex WebDriver commands into simple, reusable methods, focusing on three core pillars: **Synchronization**, **Visibility**, and **Traceability**.

[Image of Selenium WebDriver architecture diagram]

---

## ✨ Key Features

* **Smart Synchronization:** Integrated `WebDriverWait` for all interactions to eliminate "flaky" tests caused by timing issues.
* **JavaScript Fallbacks:** Uses `JavascriptExecutor` for forced clicks, smooth scrolling, and Shadow DOM interactions.
* **Visual Debugging:**
    * **Auto-Highlighting:** Elements are highlighted in yellow during interaction.
    * **Smart Screenshots:** Automated screenshot capture using Java NIO with dynamic timestamps.
* **Window Management:** Simplified tab/window switching and lifecycle management.
* **Shadow DOM Support:** Specialized methods to interact with elements hidden inside shadow roots.
* **Comprehensive State Checks:** Built-in methods to verify if elements are clickable, enabled, or disabled.

---

## 🛠️ Core Methods

### 1. Element Interaction
| Method | Description |
| :--- | :--- |
| `clickElement(String xpath)` | Waits for clickability and executes a JS-based click. |
| `sendText(String xpath, String text)` | Highlights the field, clears it, and sends the specified text. |
| `scrollAndClick(String xpath)` | Centers the element in the viewport before clicking. |

### 2. Explicit Waits
| Method | Description |
| :--- | :--- |
| `waitUntilElementVisible(xpath)` | Blocks execution until the element is present in the DOM and visible. |
| `waitUntilClickable(xpath)` | Ensures the element is ready for mouse interaction. |

### 3. Utility & Navigation
| Method | Description |
| :--- | :--- |
| `captureScreenshot(String name)` | Captures and saves a PNG to the specified screenshots directory. |
| `switchToWindowByIndex(int)` | Manages multi-window scenarios easily. |
| `findShadowElement(host, inner)` | Bridges the gap for modern Web Component testing. |

---

## 🚀 Getting Started

### Prerequisites
* **Java 11+** (Optimized for Java 17/21/25)
* **Selenium WebDriver 4.x**
* **SLF4J** for logging

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/BrowserActionsUtility.git](https://github.com/your-username/BrowserActionsUtility.git)
