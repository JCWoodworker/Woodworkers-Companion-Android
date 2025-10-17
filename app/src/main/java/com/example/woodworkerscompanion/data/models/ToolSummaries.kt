package com.example.woodworkerscompanion.data.models

object ToolSummaries {
    const val boardFootCalculator = """**What It Is**

The Board Foot Calculator is the foundational tool for every woodworker, designed to solve the most frequent mathematical task in the craft: calculating lumber volume. Hardwood lumber is sold by the board foot—a unit of volume representing one square foot of wood that is one inch thick. This calculator handles both standard industry formulas, automatically adapting whether you input length in feet or inches, and includes an advanced "Tally" feature that allows you to calculate and sum the board footage for multiple boards of varying dimensions.

**What Problem It Solves**

Accurate lumber calculation is critical for both budgeting and material planning. Buying too little means project delays and repeated trips to the lumber yard; buying too much wastes money. For beginners, the board foot formula is confusing and often leads to purchasing errors. For professionals, calculating large orders with multiple board sizes manually is time-consuming and error-prone. This tool eliminates guesswork and provides instant, accurate material requirements with integrated cost tracking, showing a running total as you build your lumber list.

**How It's Implemented**

The interface is clean and intuitive, designed for quick shop use. You input thickness (with easy nominal thickness selection like 4/4, 5/4, 6/4), width, and length, along with quantity and price per board foot. The calculator displays instant results, including total board feet and cost. The Tally feature maintains a running list of all boards you've added, making it perfect for planning complex projects or creating shopping lists. Advanced features include a customizable waste factor (typically 15-30% depending on lumber grade) to account for real-world defects and milling losses, ensuring you buy the right amount the first time.

**How It Benefits You**

This calculator transforms from a simple utility into your project planning companion. It saves you money by preventing over-purchasing and costly material shortages. The waste factor feature demonstrates professional-grade planning, helping you account for the inevitable material losses that come with defects and milling. Most importantly, the Tally feature with integrated costing gives you immediate financial feedback, allowing you to adjust your project scope or material choices before you're committed. For professionals, it seamlessly integrates with the app's inventory system, allowing you to transfer your calculated lumber list directly into your material inventory with a single tap."""

    const val cutListOptimizer = """**What It Is**

The Cut List Optimizer is an advanced computational tool that transforms your project's parts list into an efficient cutting diagram for sheet goods like plywood, MDF, and hardboard. Rather than manually arranging your cuts on a sheet—a tedious puzzle that rarely yields optimal results—this tool uses sophisticated algorithms to automatically generate the most material-efficient layout, complete with visual diagrams you can take directly to your workshop.

**What Problem It Solves**

Sheet goods are expensive, and every woodworker has experienced the frustration of poor cut planning: discovering too late that your layout wasted a large, unusable piece, or worse, that you're one cut short and need to buy another full sheet. Manual layout planning is time-consuming and mentally taxing, taking focus away from the actual craft. Even experienced woodworkers struggle to visualize the most efficient arrangement, especially when dealing with dozens of parts of varying sizes. This optimizer solves that problem by doing the complex spatial mathematics for you, often finding arrangements that would be difficult to discover manually.

**How It's Implemented**

The interface allows you to define your stock sheet dimensions (like standard 4'×8' plywood) and then input your required parts list with their dimensions. The optimizer processes this information and generates a visual cutting diagram showing exactly where to make each cut to minimize waste. Critical professional features include kerf allowance—accounting for the material removed by your saw blade—and grain direction constraints, essential when working with veneered plywood where maintaining consistent grain across all parts is crucial for aesthetic quality. The generated diagram is exportable as a PDF, so you can print it and bring it to your workbench or table saw.

**How It Benefits You**

This tool directly improves your project's profitability and sustainability. By minimizing waste, it reduces your material costs—sometimes saving an entire sheet on large projects. The time saved in planning is substantial; what might take 30 minutes of manual sketching and erasing happens in seconds. The visual diagram reduces errors at the saw, giving you confidence that your layout is correct before you make the first cut. The kerf tracking ensures dimensional accuracy across all your parts, preventing the accumulation of small errors that can ruin a project. Finally, the optimizer identifies usable offcuts—those valuable leftover pieces—and allows you to save them to your inventory system, turning what would be scrap into tracked assets for future projects."""

    const val projectPricingEngine = """**What It Is**

The Project Pricing Engine is a comprehensive business tool designed to help woodworkers systematically calculate profitable prices for their work. It's not a simple calculator but a flexible framework that guides you through all the cost components of a project—materials, labor, overhead, and profit margin—and offers multiple pricing models suited to different business types, from the casual Etsy seller to the professional custom furniture shop.

**What Problem It Solves**

Pricing is consistently cited as one of the biggest challenges for woodworkers trying to sell their creations. The most common mistake is undervaluing time and failing to account for hidden costs like consumables, tool wear, and shop overhead. Without a systematic approach, makers often price based on gut feeling, leading to unprofitable work, unsustainable business practices, and burnout. Even knowing what formula to use is confusing—should you use cost-plus? Keystone pricing? How do you account for wholesale versus retail? This tool removes the guesswork and emotion from pricing, replacing it with data-driven decision making.

**How It's Implemented**

The engine walks you through structured input fields for every cost component: material costs (which can be pulled directly from your Board Foot Calculator or Inventory), labor calculated from your hourly rate and time estimate, overhead expenses like sandpaper and finishes (typically 10-30% of materials), and your desired profit markup. Rather than forcing one approach, it offers selectable pricing templates based on proven industry models—from simple cost-plus for hobbyists to comprehensive wholesale/retail formulas for professionals. An innovative feature is the "market reasonableness check," which can help you compare your calculated price against similar items on platforms like Etsy, ensuring your pricing is competitive while still profitable.

**How It Benefits You**

This tool transforms pricing from a source of anxiety into a confident, professional process. By systematically accounting for all costs, you ensure you're not working for less than minimum wage or losing money on materials. The multiple pricing models mean the tool grows with your business; as you transition from hobbyist to professional, you can adopt more sophisticated formulas. The integration with other modules is powerful: your calculated price flows seamlessly into the Quoting system, creating a smooth workflow from estimation to client proposal. Most importantly, by helping you price work correctly, this tool makes woodworking financially sustainable, allowing you to do more of what you love without sacrificing profitability."""

    const val finishMixingCalculator = """**What It Is**

The Finish Mixing Calculator is a specialized precision tool designed to take the guesswork out of preparing traditional woodworking finishes, with a primary focus on shellac—one of the most beautiful and historically important finishes, but one that requires user mixing from flakes and solvent. The calculator handles the "pound cut" ratio system, allowing you to input your desired concentration and solvent volume, then instantly tells you exactly how much dry finish material to add for perfect results.

**What Problem It Solves**

Many premium woodworking finishes, particularly shellac, are not sold ready-to-use but require mixing from raw components like dry flakes and denatured alcohol. While this provides freshness and control, achieving the correct ratio—the "cut"—is essential for proper application, drying time, and final appearance. Too thick and the finish is difficult to apply and prone to brush marks; too thin and you need excessive coats. Calculating these ratios manually, especially for small batches, is tedious and prone to measurement errors that can waste expensive materials or ruin hours of finishing work. The traditional "pound cut" system is particularly confusing for those new to shellac.

**How It's Implemented**

The interface is deliberately simple and focused. You select your desired shellac cut from a list of standard options (1-pound cut, 2-pound cut, etc.), input the volume of solvent you want to use (with options for fluid ounces or milliliters), and the calculator instantly displays the precise weight of dry shellac flakes needed (in grams or ounces). For more modern, internationally-minded users, the calculator also supports metric-based calculations. The tool is expandable to include other multi-part finishes requiring precise mixing ratios, such as two-part epoxies for void filling or casting, effectively creating a comprehensive "finishing lab" module.

**How It Benefits You**

This tool saves time, money, and frustration in the finishing process. By providing precise measurements, it eliminates waste from incorrect mixing and ensures consistent results across all your projects. This consistency is particularly valuable if you're producing multiples of the same item for sale—your finish will look identical on every piece. For those new to traditional finishing techniques, the calculator removes a significant barrier to entry, giving you the confidence to explore beautiful classic finishes like shellac without fear of mixing errors. The ability to mix small, fresh batches means your finish is always at peak performance, and you never have old, deteriorating finish sitting on your shelf, saving money in the long run."""

    const val projectManagement = """**What It Is**

The Project Management Suite is a comprehensive digital hub designed to bring order and visibility to every aspect of your woodworking work, from initial design through final delivery. It provides a centralized dashboard for tracking all active and planned projects, with detailed task management, time tracking, expense logging, and client communication tools. This is where your projects live and breathe, transforming scattered notes and receipts into organized, actionable data.

**What Problem It Solves**

As projects grow in complexity or quantity, managing them with informal methods—sticky notes, text messages, scattered receipts, mental tracking—quickly becomes overwhelming and unsustainable. Critical details get lost, deadlines slip, material costs are forgotten, and it becomes impossible to accurately assess whether a project was actually profitable. For anyone doing custom work, managing client communication and changes is particularly challenging. The lack of systematic time tracking means you have no data to improve your estimates on future projects, perpetuating a cycle of inaccurate bidding. This suite solves all these problems by providing a single source of truth for your entire workshop operation.

**How It's Implemented**

The suite centers on a visual project dashboard showing all work at a glance, with each project displaying its current status (Quoted, Design, In Progress, Finishing, Complete) and upcoming deadlines. Within each project, you can create detailed task checklists, breaking complex builds into manageable steps and tracking completion. The time tracking feature allows you to log hours spent on each project and specific tasks, building a database of actual versus estimated time. The expense tracking system lets you record every purchase related to a project, categorized by type (materials, hardware, consumables), creating accurate job costing data. A client communication log keeps notes, decisions, and progress photos organized, preventing misunderstandings.

**How It Benefits You**

This suite transforms your woodworking operation from reactive to proactive. By seeing all projects in one dashboard, you can identify potential scheduling conflicts before they become crises and ensure nothing falls through the cracks. The time tracking data is invaluable—it shows you where your estimates are inaccurate, allowing you to bid more competitively and profitably on future work. The expense tracking creates automatic job costing reports, showing you the true profitability of each project by comparing initial estimates to actual costs. This data-driven approach helps you identify which types of projects are most profitable and which are costing you money. The deep integration with other modules—pulling material costs from Inventory, flowing pricing data into Quotes, tracking actual costs against budgets—creates a seamless workflow that makes running your woodworking business significantly more efficient and less stressful."""

    const val quotingAndInvoicing = """**What It Is**

The Professional Quoting & Invoicing Module is a complete business documentation system that creates polished, client-ready financial documents for your woodworking business. It provides customizable templates for quotes and invoices, manages your client database, tracks document status throughout the sales cycle, and handles the entire workflow from initial estimate to final payment, all while maintaining your professional brand with custom logos and formatting.

**What Problem It Solves**

For a woodworking business to be taken seriously and command professional prices, its paperwork must reflect that professionalism. Many small shop owners resort to generic word processor templates or spreadsheets, which is time-consuming, inconsistent, and can appear amateurish to potential clients. Tracking which quotes are outstanding, which invoices are overdue, and managing client information across scattered documents is inefficient and leads to lost opportunities and delayed payments. Without a systematic approach, follow-up falls through the cracks, and you may not even realize a client viewed your quote but didn't respond. This module professionalizes your entire client-facing documentation and sales process.

**How It's Implemented**

The module provides a library of clean, professional templates that you can customize with your logo and business information. Creating a quote or invoice involves a simple interface for adding line items (materials, labor, taxes, other charges), with the ability to save frequently used items for instant recall. A basic CRM (customer relationship management) system stores client contact information and project history, so returning customers are recognized and their data auto-populates. The powerful status tracking dashboard shows all your financial documents categorized as Draft, Sent, Viewed, Approved, Paid, or Overdue, giving you complete visibility into your sales pipeline and outstanding receivables at a glance.

**How It Benefits You**

This module directly impacts your cash flow and professional reputation. Well-designed, detailed quotes increase your close rate by clearly communicating value and building client confidence. The seamless workflow integration is a major time-saver: prices calculated in the Pricing Engine flow directly into quotes with one tap; approved quotes convert to invoices instantly; project details and photos from the Project Management suite can be attached to documents. The status tracking ensures you never lose track of a potential sale or forget to follow up on an overdue invoice. Most importantly, integration with online payment processors like Stripe or PayPal means clients can pay invoices instantly with a credit card, dramatically improving your cash flow and eliminating the need to chase down checks. This professionalism allows you to charge what you're worth and get paid faster."""

    const val inventoryManagement = """**What It Is**

The Integrated Inventory Management System is the most comprehensive and potentially transformative feature in the application. It's a multi-module digital asset tracking system designed to manage everything valuable in your workshop: lumber and sheet goods, hardware and consumables, tools and equipment, and even your finished product catalog. This isn't just a list—it's an intelligent system that tracks quantities, values, locations, and integrates with your project planning to show real-time stock levels and automatically calculate material costs.

**What Problem It Solves**

Woodworkers accumulate significant capital in materials and tools, but without systematic tracking, this inventory is consistently mismanaged. You buy wood you already have because you forgot about it; valuable offcuts are lost in the chaos; you run out of critical consumables mid-project; you have no idea of the actual material cost when pricing work; and you can't find that specific board when you need it. For anyone selling their work, not knowing your true Cost of Goods Sold makes profitable pricing nearly impossible. This system solves all these problems by making your physical inventory digitally searchable, tracked, and integrated with your entire workflow.

**How It's Implemented**

The system is organized into four specialized modules. The **Lumber & Materials** module tracks wood species, dimensions, board footage, cost per board foot, and physical location, with "live deduction" that automatically reduces stock when you allocate materials to a project. The **Tools & Equipment** module maintains an asset registry of all major tools, with maintenance and sharpening logs that can send reminders for recurring care, protecting your investments. The **Hardware & Consumables** module tracks quantity-based items like screws, sandpaper, and glue, with customizable reorder level alerts that notify you when stock runs low, preventing work stoppages. The **Finished Products** module catalogs completed items available for sale, with photos, descriptions, production costs (pulled automatically from completed projects), and list prices, with potential future integration to e-commerce platforms like Etsy.

**How It Benefits You**

This system is a game-changer for serious woodworkers and professionals. It eliminates redundant purchases by making your inventory instantly searchable—you'll always know what you have and where it is. The real-time stock deduction when materials are allocated to projects provides accurate availability for planning and prevents the frustration of discovering mid-build that material is already committed elsewhere. The automatic cost tracking means your material expenses flow directly into project costing and pricing, ensuring accuracy and profitability. The location tracking feature alone is worth the price of admission for anyone with a large shop or multiple storage areas. For tool maintenance, the reminder system extends the life of expensive equipment and maintains optimal performance. Most strategically, this system transforms your workshop from an opaque collection of stuff into a transparent, data-driven operation where you can make informed decisions about what to buy, what to build, and what's actually profitable."""

    const val digitalSketchpad = """**What It Is**

The Digital Sketchpad is an integrated design tool that provides a simple, touch-optimized 2D drawing canvas directly within the app. It's not intended as a replacement for advanced CAD software, but rather as a quick ideation and planning tool—the digital equivalent of the back-of-the-napkin sketch that starts most woodworking projects. It offers basic drawing tools like variable-weight pens, shapes, dimensioning capabilities, and an eraser, with all sketches saved directly within their associated project files.

**What Problem It Solves**

Most woodworking projects begin with a simple sketch to establish proportions, visualize joinery, and develop an initial parts list. While effective, paper sketches have significant limitations: they can be lost or damaged, they're difficult to modify without starting over, they can't be easily shared with clients, and they're disconnected from the rest of your project data. When your sketch is on paper and your project management is digital, you're forced to maintain parallel systems. This creates friction and the risk that your design concept and final build documentation become disconnected. The Digital Sketchpad solves this by bringing the initial design phase into the same digital ecosystem as the rest of your project.

**How It's Implemented**

The sketchpad features a clean, distraction-free canvas optimized for finger or stylus input on tablets and phones. The toolset is deliberately simple—drawing tools with adjustable line weights for different levels of detail, basic geometric shapes (lines, rectangles, circles) for quick layouts, dimensioning tools to annotate key measurements directly on the drawing, and a standard eraser. Most importantly, sketches are created and saved directly within a specific project in the Project Management Suite, ensuring your initial design concept remains permanently linked to that project's tasks, expenses, and final outcome. This creates a complete project history from first idea to finished piece.

**How It Benefits You**

This tool keeps all your project information in one place, creating a single source of truth. Your initial design sketch, material calculations, time tracking, expenses, and client communications are all together, making it easy to reference your original intent as the project evolves. For client work, the ability to sketch ideas during a consultation and immediately save them to that client's project file improves communication and reduces misunderstandings. The sketches are searchable and backed up as part of your app data, so they're never lost. While the current implementation focuses on simple 2D sketching, the architecture is designed for future expansion—potential integration with 3D modeling software like SketchUp could allow you to import 3D models and automatically generate cut lists, creating a seamless flow from design to fabrication planning."""

    const val referenceLibraries = """**What It Is**

The In-App Reference Libraries are comprehensive, searchable knowledge databases built directly into the application, providing instant access to critical information about wood species and woodworking terminology. The Wood Species Guide contains detailed profiles of common and exotic hardwoods and softwoods, complete with high-quality grain photos, physical characteristics, working properties, and common uses. The Woodworking Glossary is an A-to-Z dictionary defining the specialized language of the craft, from joinery techniques to tool names to finishing terminology.

**What Problem It Solves**

Woodworking is a deeply knowledge-intensive craft that requires familiarity with hundreds of wood species, each with unique properties affecting workability, durability, and appearance, plus a vast specialized vocabulary covering techniques, tools, and materials. Constantly interrupting your work to look up information in books or search the internet is disruptive and time-consuming. For beginners, not knowing the properties of a wood species before purchasing it can lead to expensive mistakes—buying wood that's too soft for flooring, or too hard to carve, or that will move excessively in your climate. The lack of accessible reference material creates a knowledge barrier that slows learning and reduces confidence.

**How It's Implemented**

The Wood Species Guide is organized as a searchable database where you can browse alphabetically or search by common or scientific name. Each species entry includes professional photographs showing grain pattern and color, geographic origin information, physical characteristics like color and texture, Janka hardness ratings (essential for determining durability), detailed working properties describing how the wood behaves with hand and power tools (including tendency for tear-out or splitting), and its common applications in furniture, cabinetry, flooring, or turning. The Woodworking Glossary provides clear, concise definitions of technical terms, organized alphabetically with cross-referencing between related concepts. Future enhancements could include AI-powered wood identification from photos, allowing you to identify unknown species by simply taking a picture.

**How It Benefits You**

These libraries make you a more knowledgeable and confident woodworker. Instead of gambling on unfamiliar species, you can quickly research their properties before purchasing, ensuring they're suitable for your intended application and skill level. Understanding working properties helps you anticipate challenges—you'll know if a species requires sharp tools and slow feed rates, or if it glues and finishes well. For beginners, having reliable definitions at your fingertips accelerates learning and helps you understand instructions, videos, and conversations with more experienced woodworkers. The reference materials are always with you in the shop on your phone or tablet, eliminating the need to keep physical books in your workspace or interrupt your work to search online. This instant access to knowledge transforms complex decisions about materials and techniques into confident, informed choices, ultimately improving the quality of your work and reducing costly mistakes."""
}

