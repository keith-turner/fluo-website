---
layout: page
title: Release Notes Archive
permalink: "/release-notes/"
---

Below are the release notes for all Apache Fluo releases:

{% for release in site.categories.release-notes %}
* [{{ release.version }}]({{ site.baseurl }}/release-notes/{{ release.version }}/) - {{ release.date | date_to_string }}
{% endfor %}
